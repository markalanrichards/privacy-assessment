import {
  GraphQLID,
  GraphQLInputObjectType,
  GraphQLInt,
  GraphQLNonNull,
  GraphQLObjectType,
  GraphQLString
} from "graphql";

export const customerProfileCreateType = new GraphQLInputObjectType({
  name: "customerProfileCreate",
  description: "Create a customer profile",
  fields: {
    externalEmail: {
      type: new GraphQLNonNull(GraphQLString),
      description: "Email for new customer"
    },
    externalLegalName: {
      type: new GraphQLNonNull(GraphQLString),
      description: "Name of the new customer"
    }
  }
});
export const customerProfileType = new GraphQLObjectType({
  name: "customerProfile",
  description: "Create a customer profile",
  fields: {
    id: {
      type: new GraphQLNonNull(GraphQLString),
      description: "ID of customer"
    },
    version: {
      type: new GraphQLNonNull(GraphQLString),
      description: "Version of customer"
    },
    epoch: {
      type: new GraphQLNonNull(GraphQLString),
      description: "Timestamp created"
    },
    externalEmail: {
      type: new GraphQLNonNull(GraphQLString),
      description: "Email for new customer"
    },
    externalLegalName: {
      type: new GraphQLNonNull(GraphQLString),
      description: "Name of the new customer"
    }
  }
});
