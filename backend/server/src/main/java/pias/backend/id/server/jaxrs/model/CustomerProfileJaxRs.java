package pias.backend.id.server.jaxrs.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pias.backend.id.server.entity.CustomerProfile;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class CustomerProfileJaxRs {
    private String externalEmail, externalLegalEntity, id, version, epoch;

    public CustomerProfile fromJaxRs() {
        return CustomerProfile.builder()
                .version(Long.valueOf(version))
                .id(Long.valueOf(id))
                .epoch(Long.valueOf(epoch))
                .externalLegalEntity(externalLegalEntity)
                .externalEmail(externalEmail)
                .build();
    }

    public static CustomerProfileJaxRs toJaxRs(CustomerProfile customerProfile) {
        return CustomerProfileJaxRs.builder()
                .epoch(String.valueOf(customerProfile.getEpoch()))
                .externalEmail(String.valueOf(customerProfile.getExternalEmail()))
                .id(String.valueOf(customerProfile.getId()))
                .version(String.valueOf(customerProfile.getVersion()))
                .externalLegalEntity(customerProfile.getExternalLegalEntity())
                .build();
    }

}
