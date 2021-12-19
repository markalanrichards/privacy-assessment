package pias.backend.id.server.jaxrs.model;

import pias.backend.id.server.entity.CustomerProfileUpdate;

public record CustomerProfileUpdateJaxRs(
    String externalEmail, String externalLegalEntity, String lastVersion, String id) {

  public CustomerProfileUpdate fromJaxRs() {
    return CustomerProfileUpdate.builder()
        .externalLegalEntity(externalLegalEntity)
        .externalEmail(externalEmail)
        .lastVersion(Long.valueOf(lastVersion))
        .id(Long.valueOf(id))
        .build();
  }
}
