package pias.backend.id.server.entity;

public record SubjectProfileUpdate(
    Long id,
    Long lastVersion,
    Long customerProfileId,
    String externalSubjectName,
    String externalSubjectReference) {}
