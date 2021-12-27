package pias.backend.id.test.main.web;

import java.net.URI;
import java.net.URISyntaxException;

public record UrlHelper(
    String fragment,
    String query,
    String path,
    Integer port,
    String host,
    String scheme,
    String userInfo) {
  public URI getUrl() {
    try {
      return new URI(scheme, userInfo, host, port, path, query, fragment);
    } catch (URISyntaxException e) {
      throw new IllegalStateException(e);
    }
  }
}
