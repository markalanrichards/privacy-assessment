package pias.backend.id.server.entity;

public record PrivacyConfiguration(
    int port, String hostname, JdbcConfiguration serverJdbcConfiguration, String database) {}
