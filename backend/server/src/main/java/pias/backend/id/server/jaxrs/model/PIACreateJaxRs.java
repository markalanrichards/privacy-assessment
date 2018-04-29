package pias.backend.id.server.jaxrs.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class PIACreateJaxRs {

    String subjectProfileId;
    PIADocumentJaxRs document;

}
