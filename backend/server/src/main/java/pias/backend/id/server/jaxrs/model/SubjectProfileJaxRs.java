package pias.backend.id.server.jaxrs.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pias.backend.id.server.entity.SubjectProfile;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class SubjectProfileJaxRs {
    String externalSubjectReference, externalSubjectName,  id, version, epoch, customerProfileId;

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
        return SubjectProfileJaxRs.builder()
                .epoch(String.valueOf(subjectProfile.getEpoch()))
                .id(String.valueOf(subjectProfile.getId()))
                .customerProfileId(String.valueOf(subjectProfile.getCustomerProfileId()))
                .version(String.valueOf(subjectProfile.getVersion()))
                .externalSubjectName(subjectProfile.getExternalSubjectName())
                .externalSubjectReference(subjectProfile.getExternalSubjectReference())
                .build();
    }

}
