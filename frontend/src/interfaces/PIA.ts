export interface PIAAnnex1 {
  question1: string;
  question2: string;
  question3: string;
  question4: string;
  question5: string;
  question6: string;
  question7: string;
}
export interface PIAAnnex2Step3 {
  field1: string;
  field2: string;
  field3: string;
  field4: string;
}
export interface PIAAnnex2Step4 {
  field1: string;
  field2: string;
  field3: string;
  field4: string;
}
export interface PIAAnnex2Step5 {
  field1: string;
  field2: string;
  field3: string;
}
export interface PIAAnnex2Step6 {
  field1: string;
  field2: string;
  field3: string;
}

export interface PIAAnnex2 {
  step1: string;
  step2Part1: string;
  step2Part2: string;
  step3: PIAAnnex2Step3;
  step4: PIAAnnex2Step4;
  step5: PIAAnnex2Step5;
  step6: PIAAnnex2Step6;
}
export interface PIAAnnex3Principle1 {
  question1: string;
  question2: string;
  question3: string;
  question4: string;
  question5: string;
  question6: string;
  question7: string;
  question8: string;
  question9: string;
}
export interface PIAAnnex3Principle2 {
  question1: string;
  question2: string;
}
export interface PIAAnnex3Principle3 {
  question1: string;
  question2: string;
}
export interface PIAAnnex3Principle4 {
  question1: string;
  question2: string;
}
export interface PIAAnnex3Principle5 {
  question1: string;
  question2: string;
}
export interface PIAAnnex3Principle6 {
  question1: string;
  question2: string;
}
export interface PIAAnnex3Principle7 {
  question1: string;
  question2: string;
}
export interface PIAAnnex3Principle8 {
  question1: string;
  question2: string;
}
export interface PIAAnnex3 {
  principle1: PIAAnnex3Principle1;
  principle2: PIAAnnex3Principle2;
  principle3: PIAAnnex3Principle3;
  principle4: PIAAnnex3Principle4;
  principle5: PIAAnnex3Principle5;
  principle6: PIAAnnex3Principle6;
  principle7: PIAAnnex3Principle7;
  principle8: PIAAnnex3Principle8;
}
export interface PIADocument {
  annex1: PIAAnnex1;
  annex2: PIAAnnex2;
  annex3: PIAAnnex3;
}

export interface PIACreateAvro {
  subjectProfileId: string;
  document: PIADocument;
}
export interface PIAAvro {
  subjectProfileId: string;
  document: PIADocument;

  id: string;
  version: string;
  epoch: string;
}
export interface PIAUpdateAvro {
  subjectProfileId: string;
  document: PIADocument;
  id: string;
  lastVersion: string;
}
