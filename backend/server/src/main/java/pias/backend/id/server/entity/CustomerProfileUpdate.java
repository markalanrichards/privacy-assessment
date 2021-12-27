package pias.backend.id.server.entity;

public record CustomerProfileUpdate(
    Long id, Long lastVersion, String externalEmail, String externalLegalEntity) {}
