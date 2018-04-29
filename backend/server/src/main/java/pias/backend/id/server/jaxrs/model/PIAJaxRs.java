package pias.backend.id.server.jaxrs.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class PIAJaxRs {
    String id, version, epoch, subjectProfileId;
    PIADocumentJaxRs document;

}
