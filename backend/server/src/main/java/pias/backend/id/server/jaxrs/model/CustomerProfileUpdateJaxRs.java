package pias.backend.id.server.jaxrs.model;

import pias.backend.id.server.entity.CustomerProfileUpdate;

public record CustomerProfileUpdateJaxRs(
    String externalEmail, String externalLegalEntity, String lastVersion, String id) {

  public CustomerProfileUpdate fromJaxRs() {
    final Long id = Long.valueOf(this.id);
    final Long lastVersion = Long.valueOf(this.lastVersion);
    return new CustomerProfileUpdate(id, lastVersion, externalEmail, externalLegalEntity);
  }
}
