import gql from "graphql-tag";

export const MutationCreateSubjectProfile = gql`
  mutation createSubjectProfile(
    $customerProfileId: String!
    $externalSubjectName: String!
    $externalSubjectReference: String!
  ) {
    createSubjectProfile(
      createSubjectProfile: {
        customerProfileId: $customerProfileId
        externalSubjectName: $externalSubjectName
        externalSubjectReference: $externalSubjectReference
      }
    ) {
      id
      version
      epoch
      customerProfileId
      externalSubjectName
      externalSubjectReference
    }
  }
`;

// const pasteInGUI = `mutation {
//   createSubjectProfile(
//     createSubjectProfile: {
//       customerProfileId: "123456"
//       externalSubjectName: "Test Test"
//       externalSubjectReference: "testing"
//     }
//   ) {
//     id
//     version
//     epoch
//     customerProfileId
//     externalSubjectName
//     externalSubjectReference
//   }
// }
// `;
