package pias.backend.id.server.entity;

public record SubjectProfileCreate(
    long customerProfileId, String externalSubjectName, String externalSubjectReference) {}
