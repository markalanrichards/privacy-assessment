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
