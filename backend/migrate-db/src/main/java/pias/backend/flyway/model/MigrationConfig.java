package pias.backend.flyway.model;

import pias.backend.flyway.FlywayJdbcConfig;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode
@ToString
public class MigrationConfig {
    final FlywayJdbcConfig server;
    final String database;
}
