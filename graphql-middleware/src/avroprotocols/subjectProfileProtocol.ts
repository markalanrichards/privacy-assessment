import * as avro from "avro-js/lib/protocols";
import subjectProfile from "../avpr/SubjectProfile.avpr";

export interface SubjectProfileCreateAvro {
  customerProfileId: string;
  externalSubjectName: string;
  externalSubjectReference: string;
}
export interface SubjectProfileAvro {
  customerProfileId: string;
  externalSubjectName: string;
  externalSubjectReference: string;
  id: string;
  version: string;
  epoch: string;
}
export interface SubjectProfileUpdateAvro {
  customerProfileId: string;
  externalSubjectName: string;
  externalSubjectReference: string;
  id: string;
  lastVersion: string;
}

export const protocolFactory = () => avro.createProtocol(subjectProfile);
export const subjectProfileClientFactory = clientFactory => {
  const subjectProfileClient = clientFactory(protocolFactory());
  return {
    avroCreateSubjectProfile: (
      avroCreateSubjectProfileRequest: SubjectProfileCreateAvro
    ): Promise<SubjectProfileAvro> => {
      return subjectProfileClient("avroCreateSubjectProfile", {
        request: avroCreateSubjectProfileRequest
      });
    },

    avroUpdateSubjectProfile: (
      avroUpdateSubjectProfileRequest: SubjectProfileUpdateAvro
    ): Promise<SubjectProfileAvro> =>
      subjectProfileClient("avroUpdateSubjectProfile", {
        update: avroUpdateSubjectProfileRequest
      }),
    avroReadSubjectProfile: (id): Promise<SubjectProfileAvro> =>
      subjectProfileClient("avroReadSubjectProfile", { id }),
    avroReadVersionedSubjectProfile: (
      id,
      version
    ): Promise<SubjectProfileAvro> =>
      subjectProfileClient("avroReadVersionedSubjectProfile", {
        id,
        version
      })
  };
};
