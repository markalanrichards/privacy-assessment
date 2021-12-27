package pias.backend.id.server.jaxrs.model;

import pias.backend.id.server.entity.SubjectProfile;

public record SubjectProfileJaxRs(
    String externalSubjectReference,
    String externalSubjectName,
    String id,
    String version,
    String epoch,
    String customerProfileId) {

  public SubjectProfile fromJaxRs() {
    final Long version = Long.valueOf(this.version);
    final Long id = Long.valueOf(this.id);
    final Long epoch = Long.valueOf(this.epoch);
    final Long customerProfileId = Long.valueOf(this.customerProfileId);
    return new SubjectProfile(
        id, version, epoch, customerProfileId, externalSubjectName, externalSubjectReference);
  }

  public static SubjectProfileJaxRs toJaxRs(SubjectProfile subjectProfile) {

    final String epoch = String.valueOf(subjectProfile.epoch());
    final String id = String.valueOf(subjectProfile.id());
    final String customerProfileId = String.valueOf(subjectProfile.customerProfileId());
    final String version = String.valueOf(subjectProfile.version());
    final String externalSubjectName = subjectProfile.externalSubjectName();
    final String externalSubjectReference = subjectProfile.externalSubjectReference();
    return new SubjectProfileJaxRs(
        externalSubjectReference, externalSubjectName, id, version, epoch, customerProfileId);
  }
}
