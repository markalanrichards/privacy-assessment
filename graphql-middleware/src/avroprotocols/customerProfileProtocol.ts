import * as avro from "avro-js/lib/protocols";
import customerProfile from "../avpr/CustomerProfile.avpr";

export interface CustomerProfileCreateAvro {
  externalEmail: string;
  externalLegalName: string;
}
export interface CustomerProfileAvro {
  externalEmail: string;
  externalLegalName: string;
  id: string;
  version: string;
  epoch: string;
}
export interface CustomerProfileUpdateAvro {
  externalEmail: string;
  externalLegalName: string;
  id: string;
  lastVersion: string;
}

export const protocolFactory = () => avro.createProtocol(customerProfile);
export const customerProfileClientFactory = clientFactory => {
  const customerProfileClient = clientFactory(protocolFactory());
  return {
    avroCreateCustomerProfile: (
      avroCreateCustomerProfileRequest: CustomerProfileCreateAvro
    ): Promise<CustomerProfileAvro> => {
      return customerProfileClient("avroCreateCustomerProfile", {
        request: avroCreateCustomerProfileRequest
      });
    },

    avroUpdateCustomerProfile: (
      avroUpdateCustomerProfileRequest: CustomerProfileUpdateAvro
    ): Promise<CustomerProfileAvro> =>
      customerProfileClient("avroUpdateCustomerProfile", {
        update: avroUpdateCustomerProfileRequest
      }),
    avroReadCustomerProfile: (id): Promise<CustomerProfileAvro> =>
      customerProfileClient("avroReadCustomerProfile", { id }),
    avroReadVersionedCustomerProfile: (
      id,
      version
    ): Promise<CustomerProfileAvro> =>
      customerProfileClient("avroReadVersionedCustomerProfile", {
        id,
        version
      })
  };
};
