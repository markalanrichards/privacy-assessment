package pias.backend.flyway.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import pias.backend.flyway.FlywayJdbcConfig;

@Value
@Builder
@EqualsAndHashCode
@ToString
public class MigrationConfig {
  final FlywayJdbcConfig server;
  final String database;
}
