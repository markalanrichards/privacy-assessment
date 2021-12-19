package pias.backend.id.server.jaxrs;

import org.eclipse.collections.api.list.primitive.ImmutableByteList;
import pias.backend.*;
import pias.backend.id.server.avro.PIAAvprImpl;
import pias.backend.id.server.database.PIAService;
import pias.backend.id.server.entity.PIA;
import pias.backend.id.server.entity.PIACreate;
import pias.backend.id.server.entity.PIAUpdate;
import pias.backend.id.server.jaxrs.model.*;

public class PIAServiceJaxRs implements PIAServiceInterfaceJaxRs {

  private final PIAService mysqlPiaService;
  private final PIAAvprImpl piaAvprImpl;

  public PIAServiceJaxRs(PIAService mysqlPiaService, PIAAvprImpl piaAvprImpl) {

    this.mysqlPiaService = mysqlPiaService;
    this.piaAvprImpl = piaAvprImpl;
  }

  public PIAJaxRs create(PIACreateJaxRs customerProfileCreateJaxRs) {
    PIACreate customerProfileCreate = fromJaxRs(customerProfileCreateJaxRs);
    PIA customerProfile = mysqlPiaService.create(customerProfileCreate);
    return piaJaxRsfromAvro(customerProfile);
  }

  private PIAJaxRs piaJaxRsfromAvro(PIA customerProfile) {

    final String id = String.valueOf(customerProfile.getId());
    final String epoch = String.valueOf(customerProfile.getEpoch());
    final String subjectProfileId = String.valueOf(customerProfile.getSubjectProfileId());
    final String version = String.valueOf(customerProfile.getVersion());
    final PIADocumentJaxRs document = piaDocumentJaxRsfromAvro(customerProfile.getDocument());
    return new PIAJaxRs(id, version, epoch, subjectProfileId, document);
  }

  private PIADocumentJaxRs piaDocumentJaxRsfromAvro(ImmutableByteList document) {
    final PIADocumentAvro piaDocumentAvro = piaAvprImpl.getPiaDocumentAvro(document);
    final PIAAnnex1JaxRs annex1 = piaAnnex1JaxRsfromAvro(piaDocumentAvro.getAnnex1());
    final PIAAnnex2JaxRs annex2 = piaAnnex2JaxRsFromAvro(piaDocumentAvro.getAnnex2());
    final PIAAnnex3JaxRs annex3 = piaAnnex3Principle1JaxRsfromAvro(piaDocumentAvro.getAnnex3());
    return new PIADocumentJaxRs(annex1, annex2, annex3);
  }

  private PIAAnnex3JaxRs piaAnnex3Principle1JaxRsfromAvro(PIAAnnex3Avro annex3) {

    final PIAAnnex3Principle1JaxRs principle1 =
        piaAnnex3Principle1JaxRsfromAvro(annex3.getPrinciple1());
    final PIAAnnex3Principle2JaxRs principle2 =
        piaAnnex3Principle2AvroFromAvro(annex3.getPrinciple2());
    final PIAAnnex3Principle3JaxRs principle3 =
        piaAnnex3Principle3AvroFromAvro(annex3.getPrinciple3());
    final PIAAnnex3Principle4JaxRs principle4 =
        piaAnnex3Principle4AvroFromAvro(annex3.getPrinciple4());
    final PIAAnnex3Principle5JaxRs principle5 =
        piaAnnex3Principle5AvroFromAvro(annex3.getPrinciple5());
    final PIAAnnex3Principle6JaxRs principle6 =
        piaAnnex3Principle6AvroFromAvro(annex3.getPrinciple6());
    final PIAAnnex3Principle7JaxRs principle7 =
        piaAnnex3Principle7AvroFromAvro(annex3.getPrinciple7());
    final PIAAnnex3Principle8JaxRs principle8 =
        piaAnnex3Principle8AvroFromAvro(annex3.getPrinciple8());
    return new PIAAnnex3JaxRs(
        principle1,
        principle2,
        principle3,
        principle4,
        principle5,
        principle6,
        principle7,
        principle8);
  }

  private PIAAnnex3Principle2JaxRs piaAnnex3Principle2AvroFromAvro(
      PIAAnnex3Principle2Avro principle2) {
    final String question1 = principle2.getQuestion1();
    final String question2 = principle2.getQuestion2();
    return new PIAAnnex3Principle2JaxRs(question1, question2);
  }

  private PIAAnnex3Principle3JaxRs piaAnnex3Principle3AvroFromAvro(
      PIAAnnex3Principle3Avro principle3) {
    final String question1 = principle3.getQuestion1();
    final String question2 = principle3.getQuestion2();
    return new PIAAnnex3Principle3JaxRs(question1, question2);
  }

  private PIAAnnex3Principle4JaxRs piaAnnex3Principle4AvroFromAvro(
      PIAAnnex3Principle4Avro principle4) {
    final String question1 = principle4.getQuestion1();
    final String question2 = principle4.getQuestion2();
    return new PIAAnnex3Principle4JaxRs(question1, question2);
  }

  private PIAAnnex3Principle5JaxRs piaAnnex3Principle5AvroFromAvro(
      PIAAnnex3Principle5Avro principle5) {
    final String question1 = principle5.getQuestion1();
    final String question2 = principle5.getQuestion2();
    return new PIAAnnex3Principle5JaxRs(question1, question2);
  }

  private PIAAnnex3Principle6JaxRs piaAnnex3Principle6AvroFromAvro(
      PIAAnnex3Principle6Avro principle6) {
    final String question1 = principle6.getQuestion1();
    final String question2 = principle6.getQuestion2();
    return new PIAAnnex3Principle6JaxRs(question1, question2);
  }

  private PIAAnnex3Principle7JaxRs piaAnnex3Principle7AvroFromAvro(
      PIAAnnex3Principle7Avro principle7) {
    final String question1 = principle7.getQuestion1();
    final String question2 = principle7.getQuestion2();
    return new PIAAnnex3Principle7JaxRs(question1, question2);
  }

  private PIAAnnex3Principle8JaxRs piaAnnex3Principle8AvroFromAvro(
      PIAAnnex3Principle8Avro principle8) {
    final String question1 = principle8.getQuestion1();
    final String question2 = principle8.getQuestion2();

    return new PIAAnnex3Principle8JaxRs(question1, question2);
  }

  private PIAAnnex3Principle1JaxRs piaAnnex3Principle1JaxRsfromAvro(
      PIAAnnex3Principle1Avro principle1) {

    final String question1 = principle1.getQuestion1();
    final String question2 = principle1.getQuestion2();
    final String question3 = principle1.getQuestion3();
    final String question4 = principle1.getQuestion4();
    final String question5 = principle1.getQuestion5();
    final String question6 = principle1.getQuestion6();
    final String question7 = principle1.getQuestion7();
    final String question8 = principle1.getQuestion8();
    final String question9 = principle1.getQuestion9();
    return new PIAAnnex3Principle1JaxRs(
        question1, question2, question3, question4, question5, question6, question7, question8,
        question9);
  }

  private PIAAnnex2JaxRs piaAnnex2JaxRsFromAvro(PIAAnnex2Avro annex2) {

    final String step1 = annex2.getStep1();
    final String step2Part1 = annex2.getStep2Part1();
    final String step2Part2 = annex2.getStep2Part2();
    final PIAAnnex2Step3JaxRs step3 = piaAnnex2Step3JaxRsFromAvro(annex2.getStep3());
    final PIAAnnex2Step4JaxRs step4 = piaAnnex2Step4JaxRsFromAvro(annex2.getStep4());
    final PIAAnnex2Step5JaxRs step5 = piaAnnex2Step5JaxRsFromAvro(annex2.getStep5());
    final PIAAnnex2Step6JaxRs step6 = piaAnnex2Step6JaxRsFromAvro(annex2.getStep6());
    return new PIAAnnex2JaxRs(step1, step2Part1, step2Part2, step3, step4, step5, step6);
  }

  private PIAAnnex1JaxRs piaAnnex1JaxRsfromAvro(PIAAnnex1Avro annex1) {
    final String question1 = annex1.getQuestion1();
    final String question2 = annex1.getQuestion2();
    final String question3 = annex1.getQuestion3();
    final String question4 = annex1.getQuestion4();
    final String question5 = annex1.getQuestion5();
    final String question6 = annex1.getQuestion6();
    final String question7 = annex1.getQuestion7();
    return new PIAAnnex1JaxRs(
        question1, question2, question3, question4, question5, question6, question7);
  }

  public PIADocumentAvro piaDocumentFromJaxRs(PIADocumentJaxRs piaDocumentJaxRs) {

    final PIAAnnex1Avro annex1 = piaAnnex1FromJaxRs(piaDocumentJaxRs.annex1());
    final PIAAnnex2Avro annex2 = piaAnnex2FromJaxRs(piaDocumentJaxRs.annex2());
    final PIAAnnex3Avro annex3 = piaAnnex3FromJaxRs(piaDocumentJaxRs.annex3());
    return new PIADocumentAvro(annex1, annex2, annex3);
  }

  private PIAAnnex1Avro piaAnnex1FromJaxRs(PIAAnnex1JaxRs annex1) {

    final String question1 = annex1.question1();
    final String question2 = annex1.question2();
    final String question3 = annex1.question3();
    final String question4 = annex1.question4();
    final String question5 = annex1.question5();
    final String question6 = annex1.question6();
    final String question7 = annex1.question7();
    return new PIAAnnex1Avro(
        question1, question2, question3, question4, question5, question6, question7);
  }

  private PIAAnnex2Step3JaxRs piaAnnex2Step3JaxRsFromAvro(PIAAnnex2Step3Avro piaAnnex2Step3Avro) {
    final String field1 = piaAnnex2Step3Avro.getField1();
    final String field2 = piaAnnex2Step3Avro.getField2();
    final String field3 = piaAnnex2Step3Avro.getField3();
    final String field4 = piaAnnex2Step3Avro.getField4();
    return new PIAAnnex2Step3JaxRs(field1, field2, field3, field4);
  }

  private PIAAnnex2Step4JaxRs piaAnnex2Step4JaxRsFromAvro(PIAAnnex2Step4Avro piaAnnex2Step4Avro) {

    final String field1 = piaAnnex2Step4Avro.getField1();
    final String field2 = piaAnnex2Step4Avro.getField2();
    final String field3 = piaAnnex2Step4Avro.getField3();
    final String field4 = piaAnnex2Step4Avro.getField4();
    return new PIAAnnex2Step4JaxRs(field1, field2, field3, field4);
  }

  private PIAAnnex2Step5JaxRs piaAnnex2Step5JaxRsFromAvro(PIAAnnex2Step5Avro piaAnnex2Step5Avro) {
    final String field1 = piaAnnex2Step5Avro.getField1();
    final String field2 = piaAnnex2Step5Avro.getField2();
    final String field3 = piaAnnex2Step5Avro.getField3();
    return new PIAAnnex2Step5JaxRs(field1, field2, field3);
  }

  private PIAAnnex2Step6JaxRs piaAnnex2Step6JaxRsFromAvro(PIAAnnex2Step6Avro piaAnnex2Step6Avro) {

    final String field1 = piaAnnex2Step6Avro.getField1();
    final String field2 = piaAnnex2Step6Avro.getField2();
    final String field3 = piaAnnex2Step6Avro.getField3();
    return new PIAAnnex2Step6JaxRs(field1, field2, field3);
  }

  private PIAAnnex2Step3Avro piaAnnex2Step3JaxRsFromJaxRs(PIAAnnex2Step3JaxRs piaAnnex2Step3JaxRs) {

    return PIAAnnex2Step3Avro.newBuilder()
        .setField1(piaAnnex2Step3JaxRs.field1())
        .setField2(piaAnnex2Step3JaxRs.field2())
        .setField3(piaAnnex2Step3JaxRs.field3())
        .setField4(piaAnnex2Step3JaxRs.field4())
        .build();
  }

  private PIAAnnex2Step4Avro piaAnnex2Step4JaxRsFromJaxRs(PIAAnnex2Step4JaxRs piaAnnex2Step4JaxRs) {

    return PIAAnnex2Step4Avro.newBuilder()
        .setField1(piaAnnex2Step4JaxRs.field1())
        .setField2(piaAnnex2Step4JaxRs.field2())
        .setField3(piaAnnex2Step4JaxRs.field3())
        .setField4(piaAnnex2Step4JaxRs.field4())
        .build();
  }

  private PIAAnnex2Step5Avro piaAnnex2Step5JaxRsFromJaxRs(PIAAnnex2Step5JaxRs piaAnnex2Step5JaxRs) {
    return PIAAnnex2Step5Avro.newBuilder()
        .setField1(piaAnnex2Step5JaxRs.field1())
        .setField2(piaAnnex2Step5JaxRs.field2())
        .setField3(piaAnnex2Step5JaxRs.field3())
        .build();
  }

  private PIAAnnex2Step6Avro piaAnnex2Step6JaxRsFromJaxRs(PIAAnnex2Step6JaxRs piaAnnex2Step6JaxRs) {
    return PIAAnnex2Step6Avro.newBuilder()
        .setField1(piaAnnex2Step6JaxRs.field1())
        .setField2(piaAnnex2Step6JaxRs.field2())
        .setField3(piaAnnex2Step6JaxRs.field3())
        .build();
  }

  private PIAAnnex2Avro piaAnnex2FromJaxRs(PIAAnnex2JaxRs annex2) {

    return PIAAnnex2Avro.newBuilder()
        .setStep1(annex2.step1())
        .setStep2Part1(annex2.step2Part1())
        .setStep2Part2(annex2.step2Part2())
        .setStep3(piaAnnex2Step3JaxRsFromJaxRs(annex2.step3()))
        .setStep4(piaAnnex2Step4JaxRsFromJaxRs(annex2.step4()))
        .setStep5(piaAnnex2Step5JaxRsFromJaxRs(annex2.step5()))
        .setStep6(piaAnnex2Step6JaxRsFromJaxRs(annex2.step6()))
        .build();
  }

  private PIAAnnex3Avro piaAnnex3FromJaxRs(PIAAnnex3JaxRs annex3) {

    return PIAAnnex3Avro.newBuilder()
        .setPrinciple1(piaAnnex3Principle1JaxRsFromJaxRs(annex3.principle1()))
        .setPrinciple2(piaAnnex3Principle2JaxRsFromJaxRs(annex3.principle2()))
        .setPrinciple3(piaAnnex3Principle3JaxRsFromJaxRs(annex3.principle3()))
        .setPrinciple4(piaAnnex3Principle4JaxRsFromJaxRs(annex3.principle4()))
        .setPrinciple5(piaAnnex3Principle5JaxRsFromJaxRs(annex3.principle5()))
        .setPrinciple6(piaAnnex3Principle6JaxRsFromJaxRs(annex3.principle6()))
        .setPrinciple7(piaAnnex3Principle7JaxRsFromJaxRs(annex3.principle7()))
        .setPrinciple8(piaAnnex3Principle8JaxRsFromJaxRs(annex3.principle8()))
        .build();
  }

  private PIAAnnex3Principle8Avro piaAnnex3Principle8JaxRsFromJaxRs(
      PIAAnnex3Principle8JaxRs principle8) {
    return PIAAnnex3Principle8Avro.newBuilder()
        .setQuestion1(principle8.question1())
        .setQuestion2(principle8.question2())
        .build();
  }

  private PIAAnnex3Principle7Avro piaAnnex3Principle7JaxRsFromJaxRs(
      PIAAnnex3Principle7JaxRs principle7) {

    return PIAAnnex3Principle7Avro.newBuilder()
        .setQuestion1(principle7.question1())
        .setQuestion2(principle7.question2())
        .build();
  }

  private PIAAnnex3Principle6Avro piaAnnex3Principle6JaxRsFromJaxRs(
      PIAAnnex3Principle6JaxRs principle6) {
    return PIAAnnex3Principle6Avro.newBuilder()
        .setQuestion1(principle6.question1())
        .setQuestion2(principle6.question2())
        .build();
  }

  private PIAAnnex3Principle5Avro piaAnnex3Principle5JaxRsFromJaxRs(
      PIAAnnex3Principle5JaxRs principle5) {
    return PIAAnnex3Principle5Avro.newBuilder()
        .setQuestion1(principle5.question1())
        .setQuestion2(principle5.question2())
        .build();
  }

  private PIAAnnex3Principle4Avro piaAnnex3Principle4JaxRsFromJaxRs(
      PIAAnnex3Principle4JaxRs principle4) {
    return PIAAnnex3Principle4Avro.newBuilder()
        .setQuestion1(principle4.question1())
        .setQuestion2(principle4.question2())
        .build();
  }

  private PIAAnnex3Principle3Avro piaAnnex3Principle3JaxRsFromJaxRs(
      PIAAnnex3Principle3JaxRs principle3) {
    return PIAAnnex3Principle3Avro.newBuilder()
        .setQuestion1(principle3.question1())
        .setQuestion2(principle3.question2())
        .build();
  }

  private PIAAnnex3Principle2Avro piaAnnex3Principle2JaxRsFromJaxRs(
      PIAAnnex3Principle2JaxRs principle2) {
    return PIAAnnex3Principle2Avro.newBuilder()
        .setQuestion1(principle2.question1())
        .setQuestion2(principle2.question2())
        .build();
  }

  private PIAAnnex3Principle1Avro piaAnnex3Principle1JaxRsFromJaxRs(
      PIAAnnex3Principle1JaxRs principle1) {
    return PIAAnnex3Principle1Avro.newBuilder()
        .setQuestion1(principle1.question1())
        .setQuestion2(principle1.question2())
        .setQuestion3(principle1.question3())
        .setQuestion4(principle1.question4())
        .setQuestion5(principle1.question5())
        .setQuestion6(principle1.question6())
        .setQuestion7(principle1.question7())
        .setQuestion8(principle1.question8())
        .setQuestion9(principle1.question9())
        .build();
  }

  private PIACreate fromJaxRs(PIACreateJaxRs customerProfileCreateJaxRs) {

    final ImmutableByteList serialisedDocument =
        piaAvprImpl.getSerialisedDocument(
            piaDocumentFromJaxRs(customerProfileCreateJaxRs.document()));
    return PIACreate.builder()
        .subjectProfileId(Long.valueOf(customerProfileCreateJaxRs.subjectProfileId()))
        .document(serialisedDocument)
        .build();
  }

  @Override
  public PIAJaxRs update(PIAUpdateJaxRs customerProfileUpdateJaxRs) {
    PIAUpdate customerProfileUpdate = fromJaxRs(customerProfileUpdateJaxRs);
    PIA customerProfile = mysqlPiaService.update(customerProfileUpdate);
    return piaJaxRsfromAvro(customerProfile);
  }

  private PIAUpdate fromJaxRs(PIAUpdateJaxRs customerProfileUpdateJaxRs) {
    return PIAUpdate.builder()
        .subjectProfileId(Long.valueOf(customerProfileUpdateJaxRs.subjectProfileId()))
        .id(Long.valueOf(customerProfileUpdateJaxRs.id()))
        .lastVersion(Long.valueOf(customerProfileUpdateJaxRs.lastVersion()))
        .document(
            piaAvprImpl.getSerialisedDocument(
                piaDocumentFromJaxRs(customerProfileUpdateJaxRs.document())))
        .build();
  }

  @Override
  public PIAJaxRs read(String id) {
    PIA customerProfile = mysqlPiaService.read(Long.valueOf(id));
    return piaJaxRsfromAvro(customerProfile);
  }

  @Override
  public PIAJaxRs read(String id, String version) {
    PIA customerProfile = mysqlPiaService.read(Long.valueOf(id), Long.valueOf(version));
    return piaJaxRsfromAvro(customerProfile);
  }
}
