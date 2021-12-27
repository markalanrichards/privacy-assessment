import java.net.URL;
import java.util.UUID;
import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.ResponderServlet;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.apache.avro.util.Utf8;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

public class HttpAvroServiceExternalResource<T> implements BeforeEachCallback, AfterEachCallback {
  public static Utf8 RANDOM_UTF8() {
    return new Utf8(new Utf8(UUID.randomUUID().toString()));
  }

  public static ArgumentMatcher<CharSequence> CHARSEQUENCE_MATCHER(final CharSequence toMatch) {
    return new ArgumentMatcher<CharSequence>() {
      @Override
      public boolean matches(CharSequence o) {
        return o.toString().equals(toMatch.toString());
      }
    };
  }

  private Server server;
  private HttpTransceiver nettyTransceiver;
  private T service;
  private final Class<T> clazz;
  private T client;

  public HttpAvroServiceExternalResource(Class<T> clazz) {
    this.clazz = clazz;
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {

    service = Mockito.mock(clazz);
    SpecificResponder responder = new SpecificResponder(clazz, service);
    ResponderServlet responderServlet = new ResponderServlet(responder);

    server = new Server();
    ServerConnector http = new ServerConnector(server, new HttpConnectionFactory());
    http.setPort(8080);
    server.setConnectors(new Connector[] {http});
    ServletHandler servletHandler = new ServletHandler();
    servletHandler.addServletWithMapping(new ServletHolder(responderServlet), "/test");
    new org.eclipse.jetty.servlet.ServletHandler();
    server.setHandler(servletHandler);

    server.start();

    nettyTransceiver =
        new HttpTransceiver(new URL("http://127.0.0.1:" + http.getLocalPort() + "/test"));
    client = SpecificRequestor.getClient(clazz, nettyTransceiver);
  }

  @Override
  public void afterEach(ExtensionContext context) {
    try {
      nettyTransceiver.close();
      server.stop();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public T getClient() {
    return client;
  }

  public T getService() {
    return service;
  }
}
