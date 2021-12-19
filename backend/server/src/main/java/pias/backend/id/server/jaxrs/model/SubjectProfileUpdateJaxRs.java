package pias.backend.id.server.jaxrs.model;

import pias.backend.id.server.entity.SubjectProfileUpdate;

public record SubjectProfileUpdateJaxRs(
    String externalSubjectName,
    String externalSubjectReference,
    String lastVersion,
    String id,
    String customerProfileId) {

  public SubjectProfileUpdate fromJaxRs() {
    return SubjectProfileUpdate.builder()
        .lastVersion(Long.valueOf(lastVersion))
        .id(Long.valueOf(id))
        .customerProfileId(Long.valueOf(customerProfileId))
        .externalSubjectName(externalSubjectName)
        .externalSubjectReference(externalSubjectReference)
        .build();
  }
}
