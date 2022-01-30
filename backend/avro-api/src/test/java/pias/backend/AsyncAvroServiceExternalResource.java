package pias.backend;

import async.AsyncSyncAvroRPCHandler;
import io.netty.buffer.Unpooled;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.collections.api.list.primitive.ImmutableByteList;
import org.eclipse.collections.impl.factory.primitive.ByteLists;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

public class AsyncAvroServiceExternalResource<CALLBACK>
    implements BeforeEachCallback, AfterEachCallback {
  private static final Logger LOGGER = LogManager.getLogger(AsyncAvroServiceExternalResource.class);
  private HttpTransceiver httpTransceiver;

  private ProjectReactorServerExternalResource server;
  private CALLBACK service;
  private final Class<CALLBACK> callbackClazz;
  private CALLBACK client;

  public AsyncAvroServiceExternalResource(Class<CALLBACK> callbackClazz) {
    this.callbackClazz = callbackClazz;
  }

  public static String RANDOM_UTF8() {
    return UUID.randomUUID().toString();
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    service = Mockito.mock(callbackClazz);
    AsyncSyncAvroRPCHandler<CALLBACK> callbackAsyncSyncAvroRPCHandler =
        new AsyncSyncAvroRPCHandler<>(service, callbackClazz);

    server =
        new ProjectReactorServerExternalResource(
            routes -> {
              routes.post(
                  "/avpr",
                  (req, res) -> {
                    Mono<ImmutableByteList> bytelist =
                        req.receive()
                            .aggregate()
                            .asByteArray()
                            .flatMap(
                                array -> {
                                  try {
                                    CompletableFuture<ImmutableByteList> execute =
                                        callbackAsyncSyncAvroRPCHandler.execute(
                                            ByteLists.immutable.of(array));
                                    return Mono.fromCompletionStage(execute);
                                  } catch (Exception e) {
                                    throw new RuntimeException(e);
                                  }
                                });

                    return res.send(bytelist.map(ibl -> Unpooled.copiedBuffer(ibl.toArray())));
                  });
            });
    URI uri = URI.create(server.getBaseUri() + "/avpr");
    httpTransceiver = new HttpTransceiver(uri.toURL());
    SpecificRequestor specificRequestor = new SpecificRequestor(callbackClazz, httpTransceiver);

    client = SpecificRequestor.getClient(callbackClazz, specificRequestor);
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    httpTransceiver.close();
    server.close();
  }

  public CALLBACK getClient() {
    return client;
  }

  public CALLBACK getService() {
    return service;
  }
}
