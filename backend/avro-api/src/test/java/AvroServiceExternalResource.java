import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.UUID;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.netty.NettyServer;
import org.apache.avro.ipc.netty.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.junit.rules.ExternalResource;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

public class AvroServiceExternalResource<T> extends ExternalResource {
  public static String RANDOM_UTF8() {
    return UUID.randomUUID().toString();
  }

  public static Random RANDOM = new Random();

  public static ArgumentMatcher<CharSequence> CHARSEQUENCE_MATCHER(final CharSequence toMatch) {

    return new ArgumentMatcher<CharSequence>() {
      @Override
      public boolean matches(CharSequence o) {
        return o.toString().equals(toMatch.toString());
      }
    };
  }

  private Server server;
  private NettyTransceiver nettyTransceiver;
  private T service;
  private final Class<T> clazz;
  private T client;

  public AvroServiceExternalResource(Class<T> clazz) {
    this.clazz = clazz;
  }

  @Override
  public void before() throws IOException, InterruptedException {
    service = Mockito.mock(clazz);
    server = new NettyServer(new SpecificResponder(clazz, service), new InetSocketAddress(0));

    server.start();

    nettyTransceiver = new NettyTransceiver(new InetSocketAddress(server.getPort()));
    client = SpecificRequestor.getClient(clazz, nettyTransceiver);
  }

  @Override
  public void after() {
    nettyTransceiver.close();
    server.close();
  }

  public T getClient() {
    return client;
  }

  public T getService() {
    return service;
  }
}
