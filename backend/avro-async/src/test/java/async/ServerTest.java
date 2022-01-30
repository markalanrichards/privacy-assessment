package async;

import static reactor.netty.http.client.HttpClient.create;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.commons.lang3.NotImplementedException;
import org.eclipse.collections.api.list.primitive.ImmutableByteList;
import org.eclipse.collections.impl.factory.primitive.ByteLists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pias.backend.avro.TestAvpr;
import pias.backend.avro.TestAvro;
import pias.backend.avro.TestCreateAvro;
import pias.backend.avro.TestUpdateAvro;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

public class ServerTest {

  @Test
  public void testServer() throws Exception {

    try (final ProjectReactorServerExternalResource projectReactorServerExternalResource =
        new ProjectReactorServerExternalResource(
            httpServerRoutes ->
                httpServerRoutes.post(
                    "/hello", (req, res) -> res.sendString(Mono.just("Hello world"))))) {
      String baseUri = projectReactorServerExternalResource.getBaseUri();
      final HttpClient client = create();
      String block =
          client.post().uri(baseUri + "/hello").responseContent().aggregate().asString().block();
      Assertions.assertEquals("Hello world", block);
    }
  }

  @Test
  public void testAvpr() throws Exception {
    TestAvpr.Callback callback =
        new TestAvpr.Callback() {
          @Override
          public void avroCreateTest(
              TestCreateAvro request, org.apache.avro.ipc.Callback<TestAvro> callback)
              throws IOException {
            String testField = request.getTestField();
            callback.handleResult(
                new TestAvro(
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    String.valueOf(System.currentTimeMillis()),
                    testField));
          }

          @Override
          public void avroUpdateTest(
              TestUpdateAvro update, org.apache.avro.ipc.Callback<TestAvro> callback)
              throws IOException {
            throw new NotImplementedException();
          }

          @Override
          public void avroReadTest(String id, org.apache.avro.ipc.Callback<TestAvro> callback)
              throws IOException {
            throw new NotImplementedException();
          }

          @Override
          public void avroReadVersionedTest(
              String id, String version, org.apache.avro.ipc.Callback<TestAvro> callback)
              throws IOException {
            throw new NotImplementedException();
          }

          @Override
          public TestAvro avroCreateTest(TestCreateAvro request) {
            throw new NotImplementedException();
          }

          @Override
          public TestAvro avroUpdateTest(TestUpdateAvro update) {
            throw new NotImplementedException();
          }

          @Override
          public TestAvro avroReadTest(String id) {
            throw new NotImplementedException();
          }

          @Override
          public TestAvro avroReadVersionedTest(String id, String version) {
            throw new NotImplementedException();
          }
        };
    Class<TestAvpr.Callback> clazz = TestAvpr.Callback.class;
    AsyncSyncAvroRPCHandler<TestAvpr.Callback> callbackAsyncSyncAvroRPCHandler =
        new AsyncSyncAvroRPCHandler<>(callback, TestAvpr.Callback.class);
    try (final ProjectReactorServerExternalResource projectReactorServerExternalResource =
        new ProjectReactorServerExternalResource(
            httpServerRoutes ->
                httpServerRoutes.post(
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
                    }))) {
      URI uri = URI.create(projectReactorServerExternalResource.getBaseUri() + "/avpr");
      HttpTransceiver httpTransceiver = new HttpTransceiver(uri.toURL());
      SpecificRequestor specificRequestor = new SpecificRequestor(clazz, httpTransceiver);

      TestAvpr.Callback client = SpecificRequestor.getClient(clazz, specificRequestor);

      TestAvro hello_world =
          client.avroCreateTest(TestCreateAvro.newBuilder().setTestField("hello world").build());
      Assertions.assertEquals("hello world", hello_world.getTestField());
    }
  }
}
