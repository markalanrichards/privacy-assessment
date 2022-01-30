package pias.backend.id.server;

import java.util.function.Consumer;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRoutes;

public class ProjectReactorServerExternalResource implements AutoCloseable {

  private final DisposableServer server;

  public ProjectReactorServerExternalResource(
      final String host, int port, Consumer<? super HttpServerRoutes> routesBuilder) {
    server = HttpServer.create().route(routesBuilder).host(host).port(port).bindNow();
  }

  @Override
  public void close() throws Exception {
    server.disposeNow();
  }

  public int getPort() {
    return server.port();
  }

  public String getBaseUri() {
    String host = server.host();
    int port = server.port();
    return "http://%s:%d".formatted(host, port);
  }
}
