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
public class PIADocumentJaxRs {
    public PIAAnnex1JaxRs annex1;
    public PIAAnnex2JaxRs annex2;
    public PIAAnnex3JaxRs annex3;
}
