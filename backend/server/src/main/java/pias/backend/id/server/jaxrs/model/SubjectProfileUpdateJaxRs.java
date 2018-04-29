package pias.backend.id.server.jaxrs.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pias.backend.id.server.entity.SubjectProfileUpdate;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class SubjectProfileUpdateJaxRs {
    String externalSubjectName, externalSubjectReference, lastVersion, id, customerProfileId;

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
