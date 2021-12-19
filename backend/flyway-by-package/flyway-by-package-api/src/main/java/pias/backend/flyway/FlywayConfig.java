package pias.backend.flyway;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode
@Builder(toBuilder = true)
@ToString
public class FlywayConfig {
  FlywayJdbcConfig flywayJdbcConfig;
  Class<?> classForPackage;
}
