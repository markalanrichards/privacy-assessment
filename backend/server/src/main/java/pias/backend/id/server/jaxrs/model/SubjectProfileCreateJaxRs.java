package pias.backend.id.server.jaxrs.model;

import pias.backend.id.server.entity.SubjectProfileCreate;

public record SubjectProfileCreateJaxRs(
    String externalSubjectReference, String externalSubjectName, String customerProfileId) {

  public SubjectProfileCreate fromJaxRs() {
    return SubjectProfileCreate.builder()
        .externalSubjectReference(externalSubjectReference)
        .externalSubjectName(externalSubjectName)
        .customerProfileId(Long.valueOf(customerProfileId))
        .build();
  }
}
