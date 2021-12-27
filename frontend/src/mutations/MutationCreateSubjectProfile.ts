import axios from 'axios';
import { useMutation } from 'react-query';

interface CreateSubjectProfileVaribales {
  customerProfileId: string;
  externalSubjectName: string;
  externalSubjectReference: string;
}

export function postCreateSubjectProfile() {
  return useMutation(
    'mutation',
    async (varibales: CreateSubjectProfileVaribales) => {
      const { data } = await axios.post('http://127.0.0.1:9000/mutation', {
        varibales,
      });
      return data;
    }
  );
}
