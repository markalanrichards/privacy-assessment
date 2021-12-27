package pias.backend.id.server.jaxrs.model;

import pias.backend.id.server.entity.SubjectProfileCreate;

public record SubjectProfileCreateJaxRs(
    String externalSubjectReference, String externalSubjectName, String customerProfileId) {

  public SubjectProfileCreate fromJaxRs() {

    final Long customerProfileId = Long.valueOf(this.customerProfileId);
    return new SubjectProfileCreate(
        customerProfileId, externalSubjectName, externalSubjectReference);
  }
}
