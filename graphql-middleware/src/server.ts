import * as express from 'express';
import * as graphqlHTTP from 'express-graphql';
import { GraphQLSchema } from 'graphql';

import { customerProfileClientFactory } from './avroprotocols/customerProfileProtocol';
import { piaClientFactory } from './avroprotocols/piaProtocol';
import { subjectProfileClientFactory } from './avroprotocols/subjectProfileProtocol';
import clientFactory from './client.factory';
import { customerProfileFieldsFactory } from './fields/customerProfileFields';
import { piaFieldsFactory } from './fields/piaFields';
import { subjectProfileFieldsFactory } from './fields/subjectProfileFields';
import { mutationTypeFactory } from './mutations/mutations';
import { queryTypeFactory } from './querys/querys';

import * as cors from 'cors';

export const serverFactory = (
  port: string,
  host: string,
  prefix: string,
  avprPort: string,
  avprProtocol: string
) => {
  const server = express();
  const client = clientFactory(host, prefix, avprPort, avprProtocol);
  const customerProfileClient = customerProfileClientFactory(client);
  const subjectProfileClient = subjectProfileClientFactory(client);
  const customerProfileFields = customerProfileFieldsFactory(
    customerProfileClient
  );
  const piaClient = piaClientFactory(client);
  const piaFields = piaFieldsFactory(piaClient);

  const subjectProfileFields = subjectProfileFieldsFactory(
    subjectProfileClient
  );
  const mutationType = mutationTypeFactory(
    customerProfileFields,
    subjectProfileFields,
    piaFields
  );

  const queryType = queryTypeFactory(
    customerProfileFields,
    subjectProfileFields,
    piaFields
  );

  const schema = new GraphQLSchema({
    query: queryType,
    mutation: mutationType
  });
  server.use(
    '/graphql',
    cors(),
    graphqlHTTP({
      schema,
      pretty: true,
      graphiql: true
    })
  );
  const listener = server.listen(port, () => {
    const address = listener.address();
    // tslint:disable-next-line
    console.log(`Listening on http://127.0.0.1:${address.port}`);
  });
};
