package pias.backend.id.test.main.web;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Value
@ToString
@Builder(toBuilder = true)
@EqualsAndHashCode
public class UrlHelper {
  Optional<String> fragment;
  Optional<String> query;
  Optional<String> path;
  Optional<Integer> port;
  Optional<String> host;
  Optional<String> scheme;
  Optional<String> userInfo;

  public static class UrlHelperBuilder {
    Optional<String> fragment = Optional.empty();
    Optional<String> query = Optional.empty();
    Optional<String> path = Optional.empty();
    Optional<Integer> port = Optional.empty();
    Optional<String> host = Optional.empty();
    Optional<String> scheme = Optional.empty();
    Optional<String> userInfo = Optional.empty();
  }

  public URI getUrl() {
    try {
      return new URI(
          scheme.orElse(null),
          userInfo.orElse(null),
          host.orElse(null),
          port.orElse(-1),
          path.orElse(null),
          query.orElse(null),
          fragment.orElse(null));
    } catch (URISyntaxException e) {
      throw new IllegalStateException(e);
    }
  }
}
