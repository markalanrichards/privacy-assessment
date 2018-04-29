export interface CustomerProfileCreateAvro {
  externalEmail: string;
  externalLegalName: string;
}

export interface CustomerProfileAvro {
  externalEmail: string;
  externalLegalName: string;
  id: string;
  version: string;
  epoch: string;
}
export interface CustomerProfileUpdateAvro {
  externalEmail: string;
  externalLegalName: string;
  id: string;
  lastVersion: string;
}
