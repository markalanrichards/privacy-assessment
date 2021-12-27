package pias.backend.flyway;

public record FlywayConfig(FlywayJdbcConfig flywayJdbcConfig, Class<?> classForPackage) {}
