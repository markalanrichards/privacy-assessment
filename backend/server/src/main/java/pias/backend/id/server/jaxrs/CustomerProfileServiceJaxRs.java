package pias.backend.id.server.jaxrs;

import pias.backend.id.server.database.CustomerProfileService;
import pias.backend.id.server.entity.CustomerProfile;
import pias.backend.id.server.entity.CustomerProfileCreate;
import pias.backend.id.server.entity.CustomerProfileUpdate;
import pias.backend.id.server.jaxrs.model.CustomerProfileCreateJaxRs;
import pias.backend.id.server.jaxrs.model.CustomerProfileJaxRs;
import pias.backend.id.server.jaxrs.model.CustomerProfileUpdateJaxRs;

public class CustomerProfileServiceJaxRs implements CustomerProfileServiceInterfaceJaxRs {

  private final CustomerProfileService mysqlCustomerProfileService;

  public CustomerProfileServiceJaxRs(CustomerProfileService mysqlCustomerProfileService) {
    this.mysqlCustomerProfileService = mysqlCustomerProfileService;
  }

  public CustomerProfileJaxRs create(CustomerProfileCreateJaxRs customerProfileCreateJaxRs) {
    CustomerProfileCreate customerProfileCreate = customerProfileCreateJaxRs.fromJaxRs();
    CustomerProfile customerProfile = mysqlCustomerProfileService.create(customerProfileCreate);
    return CustomerProfileJaxRs.toJaxRs(customerProfile);
  }

  @Override
  public CustomerProfileJaxRs update(CustomerProfileUpdateJaxRs customerProfileUpdateJaxRs) {
    CustomerProfileUpdate customerProfileUpdate = customerProfileUpdateJaxRs.fromJaxRs();
    CustomerProfile customerProfile = mysqlCustomerProfileService.update(customerProfileUpdate);
    return CustomerProfileJaxRs.toJaxRs(customerProfile);
  }

  @Override
  public CustomerProfileJaxRs read(String id) {
    CustomerProfile customerProfile = mysqlCustomerProfileService.read(Long.valueOf(id));
    return CustomerProfileJaxRs.toJaxRs(customerProfile);
  }

  @Override
  public CustomerProfileJaxRs read(String id, String version) {
    CustomerProfile customerProfile =
        mysqlCustomerProfileService.read(Long.valueOf(id), Long.valueOf(version));
    return CustomerProfileJaxRs.toJaxRs(customerProfile);
  }
}
