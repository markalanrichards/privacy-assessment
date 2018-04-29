package pias.backend.id.server.jaxrs.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pias.backend.id.server.entity.CustomerProfileCreate;
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class CustomerProfileCreateJaxRs {
    private String externalEmail, externalLegalEntity;

    public CustomerProfileCreate fromJaxRs() {
        return CustomerProfileCreate.builder()
                .externalLegalEntity(externalLegalEntity)
                .externalEmail(externalEmail)
                .build();
    }
}
