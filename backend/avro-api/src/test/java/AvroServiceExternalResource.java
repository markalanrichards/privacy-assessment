import java.net.URI;
import java.util.Random;
import java.util.UUID;

import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.ResponderServlet;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

public class AvroServiceExternalResource<T> implements BeforeEachCallback, AfterEachCallback {

    private HttpTransceiver httpTransceiver;

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
    private T service;
    private final Class<T> clazz;
    private T client;

    public AvroServiceExternalResource(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        service = Mockito.mock(clazz);

        final ServletHandler handler = new ServletHandler();
        server = new Server();
        final ServerConnector serverConnector = new ServerConnector(server);
        serverConnector.setHost("127.0.0.1");
        serverConnector.setPort(0);
        server.setConnectors(new Connector[]{serverConnector});
        server.setHandler(handler);
        Slf4jRequestLogWriter slf4jRequestLogWriter = new Slf4jRequestLogWriter();

        final CustomRequestLog customRequestLog =
                new CustomRequestLog(slf4jRequestLogWriter, CustomRequestLog.EXTENDED_NCSA_FORMAT);

        server.setRequestLog(customRequestLog);
        ErrorHandler errorHandler = new ErrorHandler();
        errorHandler.setShowMessageInTitle(false);
        errorHandler.setShowStacks(false);
        server.setErrorHandler(errorHandler);

        SpecificResponder responder = new SpecificResponder(clazz, service);
        ResponderServlet responderServlet = new ResponderServlet(responder);
        handler.addServletWithMapping(new ServletHolder(responderServlet), "/avpr");

        server.start();
        final ServerConnector connector = (ServerConnector) server.getConnectors()[0];

        final int port = connector.getLocalPort();
        URI uri = URI.create("http://127.0.0.1:" + port + "/avpr");

        httpTransceiver = new HttpTransceiver(uri.toURL());

        client = SpecificRequestor.getClient(clazz, httpTransceiver);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        httpTransceiver.close();
        server.stop();
    }

    public T getClient() {
        return client;
    }

    public T getService() {
        return service;
    }
}
