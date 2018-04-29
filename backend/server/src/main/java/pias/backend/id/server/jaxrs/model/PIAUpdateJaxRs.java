package pias.backend.id.server.jaxrs.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class PIAUpdateJaxRs {
    String lastVersion, id, subjectProfileId;
    PIADocumentJaxRs document;

}
