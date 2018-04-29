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
public class PIAAnnex2JaxRs {
    private String step1;
    private String step2Part1;
    private String step2Part2;
    private PIAAnnex2Step3JaxRs step3;
    private PIAAnnex2Step4JaxRs step4;
    private PIAAnnex2Step5JaxRs step5;
    private PIAAnnex2Step6JaxRs step6;


}
