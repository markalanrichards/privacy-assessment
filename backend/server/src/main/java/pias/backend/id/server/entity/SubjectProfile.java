package pias.backend.id.server.entity;

public record SubjectProfile(
    long id,
    long version,
    long epoch,
    long customerProfileId,
    String externalSubjectName,
    String externalSubjectReference) {}
