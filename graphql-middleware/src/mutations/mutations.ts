import { GraphQLObjectType } from "graphql";

export const mutationTypeFactory = (
  customerProfileFields,
  subjectProfileFields,
  piaFields
) =>
  new GraphQLObjectType({
    name: "Mutation",
    description: "The root Mutation type.",
    fields: {
      createCustomerProfile: customerProfileFields.createCustomerProfile,
      createSubjectProfile: subjectProfileFields.createSubjectProfile,
      createPIA: piaFields.createPIA
    }
  });
