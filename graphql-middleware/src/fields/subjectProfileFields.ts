import { GraphQLID, GraphQLNonNull, GraphQLString } from "graphql";
import {
  subjectProfileCreateType,
  subjectProfileType
} from "../graphql/subjectProfile";

export const subjectProfileFieldsFactory = subjectProfileClient => {
  return {
    createSubjectProfile: {
      type: subjectProfileType,
      args: {
        createSubjectProfile: {
          type: new GraphQLNonNull(subjectProfileCreateType)
        }
      },
      resolve(_, args, ctx) {
        return subjectProfileClient
          .avroCreateSubjectProfile(args.createSubjectProfile)
          .then(subjectProfile => {
            return {
              id: subjectProfile.id,
              epoch: subjectProfile.epoch,
              version: subjectProfile.version,
              customerProfileId: subjectProfile.customerProfileId,
              externalSubjectName: subjectProfile.externalSubjectName,
              externalSubjectReference: subjectProfile.externalSubjectReference
            };
          });
      }
    },
    readSubjectProfile: {
      type: subjectProfileType,
      args: {
        id: {
          type: new GraphQLNonNull(GraphQLString)
        }
      },
      resolve(_, args, ctx) {
        return subjectProfileClient
          .avroReadSubjectProfile(args.id)
          .then(subjectProfile => {
            return {
              id: subjectProfile.id,
              epoch: subjectProfile.epoch,
              version: subjectProfile.version,
              customerProfileId: subjectProfile.customerProfileId,
              externalSubjectName: subjectProfile.externalSubjectName,
              externalSubjectReference: subjectProfile.externalSubjectReference
            };
          });
      }
    }
  };
};
