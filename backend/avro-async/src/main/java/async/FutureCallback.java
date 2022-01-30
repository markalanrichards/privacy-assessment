package async;

import java.util.concurrent.CompletableFuture;
import org.apache.avro.ipc.Callback;

public class FutureCallback implements Callback<Object> {
  private final CompletableFuture<Object> future = new CompletableFuture<>();

  public CompletableFuture<Object> getFuture() {
    return future;
  }

  @Override
  public void handleResult(Object result) {
    future.complete(result);
  }

  @Override
  public void handleError(Throwable error) {
    future.completeExceptionally(error);
  }
}
