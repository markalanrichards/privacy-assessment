package pias.backend.id.test.main.web.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.util.Optional;

@Value
@Builder
@EqualsAndHashCode
@ToString
public class ResponsePojo {
  private final Optional<String> body;
  private final int code;
}
