import { CreateSubJectProfile } from '../forms/CreateSubJectProfile';
import { postCreateSubjectProfile } from '../mutations/MutationCreateSubjectProfile';

export const ConnectedCreateSubjectProfile = () => {
  const mutation = postCreateSubjectProfile();
  return (
    <>
      <h1>ConnectedCreateSubjectProfile</h1>
      <div>
        <CreateSubJectProfile mutation={mutation} />
      </div>
    </>
  );
};
