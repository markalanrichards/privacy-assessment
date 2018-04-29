import { GraphQLID, GraphQLNonNull, GraphQLString } from "graphql";
import { piaCreateType, piaType } from "../graphql/pia";

export const piaFieldsFactory = piaClient => {
  return {
    createPIA: {
      type: piaType,
      args: {
        createPIA: {
          type: new GraphQLNonNull(piaCreateType)
        }
      },
      resolve(_, args, ctx) {
        console.log(JSON.stringify(args.createPIA));
        return piaClient.avroCreatePIA(args.createPIA).then(pia => {
          return pia;
        });
      }
    },
    readPIA: {
      type: piaType,
      args: {
        id: {
          type: new GraphQLNonNull(GraphQLString)
        }
      },
      resolve(_, args, ctx) {
        return piaClient.avroReadPIA(args.id).then(pia => {
          return pia;
        });
      }
    }
  };
};
