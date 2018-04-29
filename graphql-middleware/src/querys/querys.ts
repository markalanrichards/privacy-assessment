import { GraphQLObjectType } from "graphql";

export const queryTypeFactory = (
  customerProfileFields,
  subjectProfileFields,
  piaFields
) =>
  new GraphQLObjectType({
    name: "QueryType",
    description: "The root Query type.",
    fields: {
      readCustomerProfile: customerProfileFields.readCustomerProfile,
      readSubjectProfile: subjectProfileFields.readSubjectProfile,
      readPIA: piaFields.readPIA
    }
  });
