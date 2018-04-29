interface MutateInterface<T> {
  externalLegalName: string;
  externalEmail: string;
}

export const mutate = (variables: MutateInterface<string>) => {
  return Promise.resolve({
    variables: {
      externalLegalName: variables.externalLegalName,
      externalEmail: variables.externalEmail
    }
  });
};
