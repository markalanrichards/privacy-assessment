import gql from "graphql-tag";

export const MutationCreateCustomerProfile = gql`
  mutation createCustomerProfile(
    $externalEmail: String!
    $externalLegalName: String!
  ) {
    createCustomerProfile(
      createCustomerProfile: {
        externalEmail: $externalEmail
        externalLegalName: $externalLegalName
      }
    ) {
      id
      version
      epoch
      externalEmail
      externalLegalName
    }
  }
`;

const pasteInGUI = `mutation {
  createCustomerProfile(createCustomerProfile: {externalEmail: "teset@test.com", externalLegalName: "Test Test}) {
    id
    version
    epoch
    externalEmail
    externalLegalName
  }
}`;
