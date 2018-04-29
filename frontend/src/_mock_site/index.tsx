import * as React from "react";
import { render } from "react-dom";

import { ApolloProvider } from "react-apollo";
import { ApolloClient } from "apollo-client";
import { InMemoryCache } from "apollo-cache-inmemory";
import { HttpLink } from "apollo-link-http";
import { onError } from "apollo-link-error";
import { ApolloLink } from "apollo-link";

import { ConnectedNewUserForm } from "../pages/ConnectedNewUserForm";
import { ConnectedCreateSubjectProfile } from "../pages/ConnectedCreateSubjectProfile";

const client = new ApolloClient({
  link: ApolloLink.from([
    onError(({ graphQLErrors, networkError }) => {
      if (graphQLErrors) {
        graphQLErrors.map(({ message, locations, path }) =>
          console.log(
            `[GraphQL error]: Message: ${message}, Location: ${locations}, Path: ${path}`
          )
        );
      }
      if (networkError) {
        console.log(`[Network error]: ${networkError}`);
      }
    }),
    new HttpLink({
      uri: "https://peaceful-escarpment-27512.herokuapp.com/graphql",
      credentials: "cors"
    })
  ]),
  cache: new InMemoryCache()
});

render(
  <ApolloProvider client={client}>
    <React.Fragment>
      <ConnectedNewUserForm />
      <br />
      <ConnectedCreateSubjectProfile />
    </React.Fragment>
  </ApolloProvider>,
  document.getElementById("root")
);
