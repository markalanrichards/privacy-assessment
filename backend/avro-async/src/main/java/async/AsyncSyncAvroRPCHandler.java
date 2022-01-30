package async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.apache.avro.Protocol;
import org.apache.avro.ipc.MD5;
import org.apache.avro.specific.SpecificData;
import org.eclipse.collections.api.list.primitive.ImmutableByteList;

public class AsyncSyncAvroRPCHandler<CALLBACK_T> {

  private final CallbackServiceExecutor<CALLBACK_T> callbackServiceExecutor;
  private final ResponseObjectWriterStep responseObjectWriterStep;
  private final RenderResponse renderResponse;
  private final ParseRequest parseRequest;
  private final ReadRequestContext readRequestContext;

  public AsyncSyncAvroRPCHandler(CALLBACK_T service, Class<CALLBACK_T> clazz) {
    final Protocol protocol = new SpecificData(clazz.getClassLoader()).getProtocol(clazz);
    final MD5 hash = new MD5(protocol.getMD5());
    final SpecificData specificData = new SpecificData(clazz.getClassLoader());
    callbackServiceExecutor = new CallbackServiceExecutor<>(service);
    responseObjectWriterStep = new ResponseObjectWriterStep(protocol, specificData);
    renderResponse = new RenderResponse(protocol, hash);
    parseRequest = new ParseRequest();
    readRequestContext = new ReadRequestContext(protocol, hash, specificData);
  }

  public CompletableFuture<ImmutableByteList> execute(ImmutableByteList request) throws Exception {
    return CompletableFuture.completedFuture(request)
        .thenApply(parseRequest::readToImmutableByteList)
        .thenApply(readRequestContext::readRequestcontext)
        .thenCompose(
            requestContext -> {
              Protocol.Message message = requestContext.message();
              return callbackServiceExecutor
                  .execute(message, requestContext.requestObject())
                  .thenApply(
                      responseObject -> responseObjectWriterStep.write(message, responseObject));
            })
        .thenApply(renderResponse::renderResponse)
        .orTimeout(10, TimeUnit.SECONDS);
  }
}
