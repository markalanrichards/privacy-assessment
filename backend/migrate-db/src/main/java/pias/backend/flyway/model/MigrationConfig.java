package pias.backend.flyway.model;

import pias.backend.flyway.FlywayJdbcConfig;

public record MigrationConfig(FlywayJdbcConfig server, String database) {}
