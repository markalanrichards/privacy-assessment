import {
  SubjectProfileAvro,
  SubjectProfileCreateAvro,
  SubjectProfileUpdateAvro,
} from '../interfaces/SubjectProfile';

export const mutate = (
  variables:
    | SubjectProfileAvro
    | SubjectProfileCreateAvro
    | SubjectProfileUpdateAvro
) => {
  return Promise.resolve({
    variables,
  });
};
