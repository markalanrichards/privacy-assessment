package async;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import org.apache.avro.Protocol;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.Callback;
import org.apache.avro.specific.SpecificData;

public class CallbackServiceExecutor<CALLBACK> {
  private final CALLBACK service;
  private final Class<CALLBACK> clazz;
  private final SpecificData specificData;

  public CallbackServiceExecutor(CALLBACK service) {
    this.service = service;
    clazz = (Class<CALLBACK>) service.getClass();
    specificData = new SpecificData(clazz.getClassLoader());
  }

  public CompletableFuture<Object> execute(Protocol.Message message, Object request) {
    try {
      int numParams = message.getRequest().getFields().size();
      Object[] params = new Object[numParams + 1];
      Class[] paramTypes = new Class[numParams + 1];
      int i = 0;
      FutureCallback futureCallback = new FutureCallback();
      for (Schema.Field param : message.getRequest().getFields()) {
        params[i] = ((GenericRecord) request).get(param.name());
        paramTypes[i] = specificData.getClass(param.schema());
        i++;
      }
      params[i] = futureCallback;
      paramTypes[i] = Callback.class;
      Method method = clazz.getMethod(message.getName(), paramTypes);
      method.setAccessible(true);
      method.invoke(service, params);
      return futureCallback.getFuture();

    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}
