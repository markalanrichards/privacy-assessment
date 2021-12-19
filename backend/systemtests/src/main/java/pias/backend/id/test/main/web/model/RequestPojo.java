package pias.backend.id.test.main.web.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.net.URI;
import java.util.Optional;

@Value
@Builder
@EqualsAndHashCode
@ToString
public class RequestPojo {
  private final Optional<String> body;
  private final URI uri;
  private final String method;

  public static class RequestPojoBuilder {
    private Optional<String> body = Optional.empty();
  }
}
