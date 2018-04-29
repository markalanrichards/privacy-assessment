package pias.backend.id.server.jaxrs.model;

import avro.shaded.com.google.common.cache.CacheBuilder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pias.backend.id.server.entity.CustomerProfileCreate;
import pias.backend.id.server.entity.CustomerProfileUpdate;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class CustomerProfileUpdateJaxRs {
    private String externalEmail, externalLegalEntity, lastVersion, id;

    public CustomerProfileUpdate fromJaxRs() {
        return CustomerProfileUpdate.builder()
                .externalLegalEntity(externalLegalEntity)
                .externalEmail(externalEmail)
                .lastVersion(Long.valueOf(lastVersion))
                .id(Long.valueOf(id))
                .build();
    }
}
