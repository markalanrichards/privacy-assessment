package pias.backend;

import java.util.function.Consumer;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRoutes;

public class ProjectReactorServerExternalResource implements AutoCloseable {

  private final DisposableServer server;

  public ProjectReactorServerExternalResource(Consumer<? super HttpServerRoutes> routesBuilder) {
    server = HttpServer.create().route(routesBuilder).host("127.0.0.1").bindNow();
    ;
  }

  @Override
  public void close() throws Exception {
    server.disposeNow();
  }

  public String getBaseUri() {
    String host = server.host();
    int port = server.port();
    return "http://%s:%d".formatted(host, port);
  }
}
