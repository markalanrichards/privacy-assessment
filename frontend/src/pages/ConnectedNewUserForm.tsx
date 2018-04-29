import * as React from "react";
import { Error } from "../atoms/Error";
import { Loading } from "../atoms/Loading";
import { graphql, ChildProps } from "react-apollo";
import { Mutation } from "react-apollo";

import { CreateCustomerProfile } from "../components/CreateCustomerProfile";
import { MutationCreateCustomerProfile } from "../mutations/MutationCreateCustomerProfile";
import {
  CustomerProfileAvro,
  CustomerProfileCreateAvro,
  CustomerProfileUpdateAvro
} from "../interfaces/CustomerProfile";

interface InputProps extends CustomerProfileCreateAvro {
  loading: boolean | null;
  error: boolean | null;
}

interface ConfirmDataInterface {
  createCustomerProfile: CustomerProfileAvro;
}

const ConfirmData = ({ createCustomerProfile }: ConfirmDataInterface) => {
  const {
    externalLegalName,
    externalEmail,
    epoch,
    id,
    version
  } = createCustomerProfile;
  return (
    <React.Fragment>
      <div>externalLegalName: {externalLegalName}</div>
      <div>externalEmail: {externalEmail}</div>
      <div>epoch: {epoch}</div>
      <div>id: {id}</div>
      <div>version: {version}</div>
    </React.Fragment>
  );
};

export const ConnectedNewUserForm = () => (
  <React.Fragment>
    <h1>ConnectedNewUserForm</h1>
    <Mutation mutation={MutationCreateCustomerProfile}>
      {(createCustomerProfile, { loading, error, data }) => (
        <div>
          <CreateCustomerProfile mutate={createCustomerProfile} />
          {loading ? <Loading /> : null}
          {data ? <ConfirmData {...data} /> : null}
          {error ? <Error {...error} /> : null}
        </div>
      )}
    </Mutation>
  </React.Fragment>
);
