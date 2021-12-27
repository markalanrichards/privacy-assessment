package pias.backend.id.server.jaxrs.model;

import pias.backend.id.server.entity.CustomerProfile;

public record CustomerProfileJaxRs(
    String externalEmail, String externalLegalEntity, String id, String version, String epoch) {

  public CustomerProfile fromJaxRs() {

    final Long version = Long.valueOf(this.version);
    final Long id = Long.valueOf(this.id);
    final Long epoch = Long.valueOf(this.epoch);
    return new CustomerProfile(id, version, epoch, externalEmail, externalLegalEntity);
  }

  public static CustomerProfileJaxRs toJaxRs(CustomerProfile customerProfile) {
    final String externalEmail = String.valueOf(customerProfile.externalEmail());
    final String id = String.valueOf(customerProfile.id());
    final String epoch = String.valueOf(customerProfile.epoch());
    final String version = String.valueOf(customerProfile.version());
    final String externalLegalEntity = customerProfile.externalLegalEntity();
    return new CustomerProfileJaxRs(externalEmail, externalLegalEntity, id, version, epoch);
  }
}
