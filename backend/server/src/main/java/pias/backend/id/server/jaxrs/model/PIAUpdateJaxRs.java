package pias.backend.id.server.jaxrs.model;

public record PIAUpdateJaxRs(
    String lastVersion, String id, String subjectProfileId, PIADocumentJaxRs document) {}
