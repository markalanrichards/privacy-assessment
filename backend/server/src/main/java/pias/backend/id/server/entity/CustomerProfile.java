package pias.backend.id.server.entity;

public record CustomerProfile(
    long id, long version, long epoch, String externalEmail, String externalLegalEntity) {}
