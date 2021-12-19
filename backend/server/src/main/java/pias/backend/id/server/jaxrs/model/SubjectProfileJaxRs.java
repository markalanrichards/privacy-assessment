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
    return SubjectProfile.builder()
        .version(Long.valueOf(version))
        .id(Long.valueOf(id))
        .epoch(Long.valueOf(epoch))
        .customerProfileId(Long.valueOf(customerProfileId))
        .externalSubjectReference(externalSubjectReference)
        .externalSubjectName(externalSubjectName)
        .build();
  }

  public static SubjectProfileJaxRs toJaxRs(SubjectProfile subjectProfile) {

    final String epoch = String.valueOf(subjectProfile.getEpoch());
    final String id = String.valueOf(subjectProfile.getId());
    final String customerProfileId = String.valueOf(subjectProfile.getCustomerProfileId());
    final String version = String.valueOf(subjectProfile.getVersion());
    final String externalSubjectName = subjectProfile.getExternalSubjectName();
    final String externalSubjectReference = subjectProfile.getExternalSubjectReference();
    return new SubjectProfileJaxRs(
        externalSubjectReference, externalSubjectName, id, version, epoch, customerProfileId);
  }
}
