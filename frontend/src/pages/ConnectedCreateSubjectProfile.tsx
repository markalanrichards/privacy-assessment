import * as React from "react";
import { Error } from "../atoms/Error";
import { Loading } from "../atoms/Loading";
import { graphql, ChildProps } from "react-apollo";

import { Mutation } from "react-apollo";

import { CreateSubJectProfile } from "../components/CreateSubJectProfile";
import { MutationCreateSubjectProfile } from "../mutations/MutationCreateSubjectProfile";

import {
  SubjectProfileAvro,
  SubjectProfileCreateAvro,
  SubjectProfileUpdateAvro
} from "../interfaces/SubjectProfile";

interface InputProps extends SubjectProfileCreateAvro {
  loading: boolean | null;
  error: boolean | null;
}

interface ConfirmDataInterface {
  createSubjectProfile: SubjectProfileAvro;
}

const ConfirmData = ({ createSubjectProfile }: ConfirmDataInterface) => {
  const {
    id,
    version,
    epoch,
    customerProfileId,
    externalSubjectName,
    externalSubjectReference
  } = createSubjectProfile;
  return (
    <React.Fragment>
      <div>externalSubjectName: {externalSubjectName}</div>
      <div>externalSubjectReference: {externalSubjectReference}</div>
      <div>epoch: {epoch}</div>
      <div>id: {id}</div>
      <div>version: {version}</div>
    </React.Fragment>
  );
};

export const ConnectedCreateSubjectProfile = () => (
  <React.Fragment>
    <h1>ConnectedCreateSubjectProfile</h1>
    <Mutation mutation={MutationCreateSubjectProfile}>
      {(createSubjectProfile, { loading, error, data }) => (
        <div>
          <CreateSubJectProfile mutate={createSubjectProfile} />
          {loading ? <Loading /> : null}
          {data ? <ConfirmData {...data} /> : null}
          {error ? <Error {...error} /> : null}
        </div>
      )}
    </Mutation>
  </React.Fragment>
);
