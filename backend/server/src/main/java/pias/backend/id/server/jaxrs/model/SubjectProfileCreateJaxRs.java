package pias.backend.id.server.jaxrs.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pias.backend.id.server.entity.SubjectProfileCreate;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class SubjectProfileCreateJaxRs {
    String externalSubjectReference, externalSubjectName, customerProfileId;

    public SubjectProfileCreate fromJaxRs() {
        return SubjectProfileCreate.builder()
                .externalSubjectReference(externalSubjectReference)
                .externalSubjectName(externalSubjectName)
                .customerProfileId(Long.valueOf(customerProfileId))
                .build();
    }
}
