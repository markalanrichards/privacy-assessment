package pias.backend.id.server.jaxrs.model;

import pias.backend.id.server.entity.SubjectProfileUpdate;

public record SubjectProfileUpdateJaxRs(
    String externalSubjectName,
    String externalSubjectReference,
    String lastVersion,
    String id,
    String customerProfileId) {

  public SubjectProfileUpdate fromJaxRs() {

    final Long lastVersion = Long.valueOf(this.lastVersion);
    final Long id = Long.valueOf(this.id);
    final Long customerProfileId = Long.valueOf(this.customerProfileId);
    return new SubjectProfileUpdate(
        id, lastVersion, customerProfileId, externalSubjectName, externalSubjectReference);
  }
}
