import axios from 'axios';
import { useMutation } from 'react-query';

interface CreateCustomerProfile {
  externalEmail: string;
  externalLegalName: string;
}

export function postCreateCustomerProfile() {
  return useMutation('mutation', async (varibales: CreateCustomerProfile) => {
    const { data } = await axios.post('http://127.0.0.1:9000/mutation', {
      varibales,
    });
    return data;
  });
}
