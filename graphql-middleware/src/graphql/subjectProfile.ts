import {
  GraphQLID,
  GraphQLInputObjectType,
  GraphQLInt,
  GraphQLNonNull,
  GraphQLObjectType,
  GraphQLString
} from "graphql";

export const subjectProfileCreateType = new GraphQLInputObjectType({
  name: "subjectProfileCreate",
  description: "Create a subject profile",
  fields: {
    customerProfileId: {
      type: new GraphQLNonNull(GraphQLString),
      description: "Email for new subject"
    },
    externalSubjectName: {
      type: new GraphQLNonNull(GraphQLString),
      description: "Name of the new subject"
    },
    externalSubjectReference: {
      type: new GraphQLNonNull(GraphQLString),
      description: "Name of the new subject"
    }
  }
});
export const subjectProfileType = new GraphQLObjectType({
  name: "subjectProfile",
  description: "Create a subject profile",
  fields: {
    id: {
      type: new GraphQLNonNull(GraphQLString),
      description: "ID of subject"
    },
    version: {
      type: new GraphQLNonNull(GraphQLString),
      description: "Version of subject"
    },
    epoch: {
      type: new GraphQLNonNull(GraphQLString),
      description: "Timestamp created"
    },
    customerProfileId: {
      type: new GraphQLNonNull(GraphQLString),
      description: "Customer Id"
    },
    externalSubjectName: {
      type: new GraphQLNonNull(GraphQLString),
      description: "Subject Namer"
    },
    externalSubjectReference: {
      type: new GraphQLNonNull(GraphQLString),
      description: "Subject Reference (url)"
    }
  }
});
