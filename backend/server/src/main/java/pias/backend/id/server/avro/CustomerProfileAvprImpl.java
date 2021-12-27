package pias.backend.id.server.avro;

import pias.backend.avro.CustomerProfileAvpr;
import pias.backend.avro.CustomerProfileAvro;
import pias.backend.avro.CustomerProfileCreateAvro;
import pias.backend.avro.CustomerProfileUpdateAvro;
import pias.backend.id.server.database.CustomerProfileService;
import pias.backend.id.server.entity.CustomerProfile;
import pias.backend.id.server.entity.CustomerProfileCreate;
import pias.backend.id.server.entity.CustomerProfileUpdate;

public class CustomerProfileAvprImpl implements CustomerProfileAvpr {
  private final CustomerProfileService mysqlCustomerProfileService;

  public CustomerProfileAvprImpl(CustomerProfileService mysqlCustomerProfileService) {
    this.mysqlCustomerProfileService = mysqlCustomerProfileService;
  }

  @Override
  public CustomerProfileAvro avroCreateCustomerProfile(CustomerProfileCreateAvro request) {
    final CustomerProfileCreate customerProfileCreate =
        new CustomerProfileCreate(request.getExternalEmail(), request.getExternalLegalName());
    return convertToAvro(mysqlCustomerProfileService.create(customerProfileCreate));
  }

  @Override
  public CustomerProfileAvro avroUpdateCustomerProfile(CustomerProfileUpdateAvro update) {

    final Long id = Long.valueOf(update.getId());
    final Long lastVersion = Long.valueOf(update.getLastVersion());
    final String externalLegalEntity = update.getExternalLegalName();
    final String externalEmail = update.getExternalEmail();
    final CustomerProfileUpdate customerProfileUpdate =
        new CustomerProfileUpdate(id, lastVersion, externalEmail, externalLegalEntity);
    final CustomerProfile updatedCustomerProfile =
        mysqlCustomerProfileService.update(customerProfileUpdate);
    return convertToAvro(updatedCustomerProfile);
  }

  @Override
  public CustomerProfileAvro avroReadCustomerProfile(String id) {
    final CustomerProfile read = mysqlCustomerProfileService.read(Long.valueOf(id));
    return convertToAvro(read);
  }

  private CustomerProfileAvro convertToAvro(CustomerProfile read) {
    final String externalLegalEntity = read.externalLegalEntity();
    final String externalEmail = read.externalEmail();
    final String id = String.valueOf(read.id());
    final String epoch = String.valueOf(read.epoch());
    final String version = String.valueOf(read.version());
    return CustomerProfileAvro.newBuilder()
        .setExternalLegalName(externalLegalEntity)
        .setExternalEmail(externalEmail)
        .setId(id)
        .setVersion(version)
        .setEpoch(epoch)
        .build();
  }

  @Override
  public CustomerProfileAvro avroReadVersionedCustomerProfile(String id, String version) {
    final CustomerProfile read =
        mysqlCustomerProfileService.read(Long.valueOf(id), Long.valueOf(version));
    return convertToAvro(read);
  }
}
