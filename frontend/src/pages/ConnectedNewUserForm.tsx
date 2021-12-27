import { CreateCustomerProfile } from '../forms/CreateCustomerProfile';
import { postCreateCustomerProfile } from '../mutations/MutationCreateCustomerProfile';

export const ConnectedNewUserForm = () => {
  const mutation = postCreateCustomerProfile();
  return (
    <>
      <h1>ConnectedNewUserForm</h1>
      <div>
        <CreateCustomerProfile mutation={mutation} />
      </div>
    </>
  );
};
