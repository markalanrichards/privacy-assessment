package pias.backend.id.server.jaxrs.model;

import pias.backend.id.server.entity.CustomerProfileCreate;

public record CustomerProfileCreateJaxRs(String externalEmail, String externalLegalEntity) {

  public CustomerProfileCreate fromJaxRs() {
    return CustomerProfileCreate.builder()
        .externalLegalEntity(externalLegalEntity)
        .externalEmail(externalEmail)
        .build();
  }
}
