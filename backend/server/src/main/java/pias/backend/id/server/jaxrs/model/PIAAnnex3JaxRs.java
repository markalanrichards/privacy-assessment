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
public class PIAAnnex3JaxRs {
    PIAAnnex3Principle1JaxRs principle1;
    PIAAnnex3Principle2JaxRs principle2;
    PIAAnnex3Principle3JaxRs principle3;
    PIAAnnex3Principle4JaxRs principle4;
    PIAAnnex3Principle5JaxRs principle5;
    PIAAnnex3Principle6JaxRs principle6;
    PIAAnnex3Principle7JaxRs principle7;
    PIAAnnex3Principle8JaxRs principle8;

}
