import { GraphQLID, GraphQLNonNull, GraphQLString } from "graphql";
import {
  customerProfileCreateType,
  customerProfileType
} from "../graphql/customerProfile";

export const customerProfileFieldsFactory = customerProfileClient => {
  return {
    createCustomerProfile: {
      type: customerProfileType,
      args: {
        createCustomerProfile: {
          type: new GraphQLNonNull(customerProfileCreateType)
        }
      },
      resolve(_, args, ctx) {
        return customerProfileClient
          .avroCreateCustomerProfile(args.createCustomerProfile)
          .then(customerProfile => {
            return {
              id: customerProfile.id,
              epoch: customerProfile.epoch,
              version: customerProfile.version,
              externalEmail: customerProfile.externalEmail,
              externalLegalName: customerProfile.externalLegalName
            };
          });
      }
    },
    readCustomerProfile: {
      type: customerProfileType,
      args: {
        id: {
          type: new GraphQLNonNull(GraphQLString)
        }
      },
      resolve(_, args, ctx) {
        return customerProfileClient
          .avroReadCustomerProfile(args.id)
          .then(customerProfile => {
            return {
              id: customerProfile.id,
              epoch: customerProfile.epoch,
              version: customerProfile.version,
              externalEmail: customerProfile.externalEmail,
              externalLegalName: customerProfile.externalLegalName
            };
          });
      }
    }
  };
};
