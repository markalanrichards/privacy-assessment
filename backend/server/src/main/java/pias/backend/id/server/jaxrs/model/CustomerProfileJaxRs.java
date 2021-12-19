package pias.backend.id.server.jaxrs.model;

import pias.backend.id.server.entity.CustomerProfile;

public record CustomerProfileJaxRs(
    String externalEmail, String externalLegalEntity, String id, String version, String epoch) {

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
    final String externalEmail = String.valueOf(customerProfile.getExternalEmail());
    final String id = String.valueOf(customerProfile.getId());
    final String epoch = String.valueOf(customerProfile.getEpoch());
    final String version = String.valueOf(customerProfile.getVersion());
    final String externalLegalEntity = customerProfile.getExternalLegalEntity();
    return new CustomerProfileJaxRs(externalEmail, externalLegalEntity, id, version, epoch);
  }
}
