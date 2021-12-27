package pias.backend.flyway;

public record FlywayJdbcConfig(
    String jdbcUrl, String user, String password, String initSql, long timeoutSeconds) {}
