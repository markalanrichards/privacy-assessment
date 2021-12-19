package pias.backend.id.server.jaxrs.model;

public record PIAJaxRs(
    String id, String version, String epoch, String subjectProfileId, PIADocumentJaxRs document) {}
