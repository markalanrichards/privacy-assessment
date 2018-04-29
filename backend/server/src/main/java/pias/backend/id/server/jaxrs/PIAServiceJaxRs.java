package pias.backend.id.server.jaxrs;

import org.eclipse.collections.api.list.primitive.ImmutableByteList;
import pias.backend.PIAAnnex1Avro;
import pias.backend.PIAAnnex2Avro;
import pias.backend.PIAAnnex2Step3Avro;
import pias.backend.PIAAnnex2Step4Avro;
import pias.backend.PIAAnnex2Step5Avro;
import pias.backend.PIAAnnex2Step6Avro;
import pias.backend.PIAAnnex3Avro;
import pias.backend.PIAAnnex3Principle1Avro;
import pias.backend.PIAAnnex3Principle2Avro;
import pias.backend.PIAAnnex3Principle3Avro;
import pias.backend.PIAAnnex3Principle4Avro;
import pias.backend.PIAAnnex3Principle5Avro;
import pias.backend.PIAAnnex3Principle6Avro;
import pias.backend.PIAAnnex3Principle7Avro;
import pias.backend.PIAAnnex3Principle8Avro;
import pias.backend.PIADocumentAvro;
import pias.backend.id.server.avro.PIAAvprImpl;
import pias.backend.id.server.database.PIAService;
import pias.backend.id.server.mysql.MysqlPIAService;
import pias.backend.id.server.entity.PIA;
import pias.backend.id.server.entity.PIACreate;
import pias.backend.id.server.entity.PIAUpdate;
import pias.backend.id.server.jaxrs.model.PIAAnnex1JaxRs;
import pias.backend.id.server.jaxrs.model.PIAAnnex2JaxRs;
import pias.backend.id.server.jaxrs.model.PIAAnnex2Step3JaxRs;
import pias.backend.id.server.jaxrs.model.PIAAnnex2Step4JaxRs;
import pias.backend.id.server.jaxrs.model.PIAAnnex2Step5JaxRs;
import pias.backend.id.server.jaxrs.model.PIAAnnex2Step6JaxRs;
import pias.backend.id.server.jaxrs.model.PIAAnnex3JaxRs;
import pias.backend.id.server.jaxrs.model.PIAAnnex3Principle1JaxRs;
import pias.backend.id.server.jaxrs.model.PIAAnnex3Principle2JaxRs;
import pias.backend.id.server.jaxrs.model.PIAAnnex3Principle3JaxRs;
import pias.backend.id.server.jaxrs.model.PIAAnnex3Principle4JaxRs;
import pias.backend.id.server.jaxrs.model.PIAAnnex3Principle5JaxRs;
import pias.backend.id.server.jaxrs.model.PIAAnnex3Principle6JaxRs;
import pias.backend.id.server.jaxrs.model.PIAAnnex3Principle7JaxRs;
import pias.backend.id.server.jaxrs.model.PIAAnnex3Principle8JaxRs;
import pias.backend.id.server.jaxrs.model.PIACreateJaxRs;
import pias.backend.id.server.jaxrs.model.PIADocumentJaxRs;
import pias.backend.id.server.jaxrs.model.PIAJaxRs;
import pias.backend.id.server.jaxrs.model.PIAUpdateJaxRs;

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
        return PIAJaxRs.builder()
                .id(String.valueOf(customerProfile.getId()))
                .epoch(String.valueOf(customerProfile.getEpoch()))
                .subjectProfileId(String.valueOf(customerProfile.getSubjectProfileId()))
                .version(String.valueOf(customerProfile.getVersion()))
                .document(piaDocumentJaxRsfromAvro(customerProfile.getDocument()))
                .build();
    }

    private PIADocumentJaxRs piaDocumentJaxRsfromAvro(ImmutableByteList document) {
        final PIADocumentAvro piaDocumentAvro = piaAvprImpl.getPiaDocumentAvro(document);
        return PIADocumentJaxRs.builder()
                .annex1(piaAnnex1JaxRsfromAvro(piaDocumentAvro.getAnnex1()))
                .annex2(piaAnnex2JaxRsFromAvro(piaDocumentAvro.getAnnex2()))
                .annex3(piaAnnex3Principle1JaxRsfromAvro(piaDocumentAvro.getAnnex3()))
                .build();
    }

    private PIAAnnex3JaxRs piaAnnex3Principle1JaxRsfromAvro(PIAAnnex3Avro annex3) {

        return PIAAnnex3JaxRs.builder()
                .principle1(piaAnnex3Principle1JaxRsfromAvro(annex3.getPrinciple1()))
                .principle2(piaAnnex3Principle2AvroFromAvro(annex3.getPrinciple2()))
                .principle3(piaAnnex3Principle3AvroFromAvro(annex3.getPrinciple3()))
                .principle4(piaAnnex3Principle4AvroFromAvro(annex3.getPrinciple4()))
                .principle5(piaAnnex3Principle5AvroFromAvro(annex3.getPrinciple5()))
                .principle6(piaAnnex3Principle6AvroFromAvro(annex3.getPrinciple6()))
                .principle7(piaAnnex3Principle7AvroFromAvro(annex3.getPrinciple7()))
                .principle8(piaAnnex3Principle8AvroFromAvro(annex3.getPrinciple8()))
                .build();
    }

    private PIAAnnex3Principle2JaxRs piaAnnex3Principle2AvroFromAvro(PIAAnnex3Principle2Avro principle2) {
        return PIAAnnex3Principle2JaxRs.builder()
                .question1(principle2.getQuestion1())
                .question2(principle2.getQuestion2())
                .build();
    }

    private PIAAnnex3Principle3JaxRs piaAnnex3Principle3AvroFromAvro(PIAAnnex3Principle3Avro principle3) {
        return PIAAnnex3Principle3JaxRs.builder()
                .question1(principle3.getQuestion1())
                .question2(principle3.getQuestion2())
                .build();
    }

    private PIAAnnex3Principle4JaxRs piaAnnex3Principle4AvroFromAvro(PIAAnnex3Principle4Avro principle4) {
        return PIAAnnex3Principle4JaxRs.builder()
                .question1(principle4.getQuestion1())
                .question2(principle4.getQuestion2())
                .build();
    }

    private PIAAnnex3Principle5JaxRs piaAnnex3Principle5AvroFromAvro(PIAAnnex3Principle5Avro principle5) {
        return PIAAnnex3Principle5JaxRs.builder()
                .question1(principle5.getQuestion1())
                .question2(principle5.getQuestion2())
                .build();
    }

    private PIAAnnex3Principle6JaxRs piaAnnex3Principle6AvroFromAvro(PIAAnnex3Principle6Avro principle6) {
        return PIAAnnex3Principle6JaxRs.builder()
                .question1(principle6.getQuestion1())
                .question2(principle6.getQuestion2())
                .build();
    }

    private PIAAnnex3Principle7JaxRs piaAnnex3Principle7AvroFromAvro(PIAAnnex3Principle7Avro principle7) {
        return PIAAnnex3Principle7JaxRs.builder()
                .question1(principle7.getQuestion1())
                .question2(principle7.getQuestion2())
                .build();
    }

    private PIAAnnex3Principle8JaxRs piaAnnex3Principle8AvroFromAvro(PIAAnnex3Principle8Avro principle8) {
        return PIAAnnex3Principle8JaxRs.builder()
                .question1(principle8.getQuestion1())
                .question2(principle8.getQuestion2())
                .build();
    }

    private PIAAnnex3Principle1JaxRs piaAnnex3Principle1JaxRsfromAvro(PIAAnnex3Principle1Avro principle1) {
        return PIAAnnex3Principle1JaxRs.builder()
                .question1(principle1.getQuestion1())
                .question2(principle1.getQuestion2())
                .question3(principle1.getQuestion3())
                .question4(principle1.getQuestion4())
                .question5(principle1.getQuestion5())
                .question6(principle1.getQuestion6())
                .question7(principle1.getQuestion7())
                .question8(principle1.getQuestion8())
                .question9(principle1.getQuestion9())
                .build();
    }

    private PIAAnnex2JaxRs piaAnnex2JaxRsFromAvro(PIAAnnex2Avro annex2) {
        return PIAAnnex2JaxRs.builder()
                .step1(annex2.getStep1())
                .step2Part1(annex2.getStep2Part1())
                .step2Part2(annex2.getStep2Part2())
                .step3(piaAnnex2Step3JaxRsFromAvro(annex2.getStep3()))
                .step4(piaAnnex2Step4JaxRsFromAvro(annex2.getStep4()))
                .step5(piaAnnex2Step5JaxRsFromAvro(annex2.getStep5()))
                .step6(piaAnnex2Step6JaxRsFromAvro(annex2.getStep6()))


                .build();
    }

    private PIAAnnex1JaxRs piaAnnex1JaxRsfromAvro(PIAAnnex1Avro annex1) {
        return PIAAnnex1JaxRs.builder()
                .question1(annex1.getQuestion1())
                .question2(annex1.getQuestion2())
                .question3(annex1.getQuestion3())
                .question4(annex1.getQuestion4())
                .question5(annex1.getQuestion5())
                .question6(annex1.getQuestion6())
                .question7(annex1.getQuestion7())
                .build();
    }

    public PIADocumentAvro piaDocumentFromJaxRs(PIADocumentJaxRs piaDocumentJaxRs) {
        return PIADocumentAvro.newBuilder()
                .setAnnex1(piaAnnex1FromJaxRs(piaDocumentJaxRs.getAnnex1()))
                .setAnnex2(piaAnnex2FromJaxRs(piaDocumentJaxRs.getAnnex2()))
                .setAnnex3(piaAnnex3FromJaxRs(piaDocumentJaxRs.getAnnex3()))
                .build();
    }

    private PIAAnnex1Avro piaAnnex1FromJaxRs(PIAAnnex1JaxRs annex1) {
        return PIAAnnex1Avro.newBuilder()
                .setQuestion1(annex1.getQuestion1())
                .setQuestion2(annex1.getQuestion2())
                .setQuestion3(annex1.getQuestion3())
                .setQuestion4(annex1.getQuestion4())
                .setQuestion5(annex1.getQuestion5())
                .setQuestion6(annex1.getQuestion6())
                .setQuestion7(annex1.getQuestion7())
                .build();
    }

    private PIAAnnex2Step3JaxRs piaAnnex2Step3JaxRsFromAvro(PIAAnnex2Step3Avro piaAnnex2Step3Avro) {

        return PIAAnnex2Step3JaxRs.builder()
                .field1(piaAnnex2Step3Avro.getField1())
                .field2(piaAnnex2Step3Avro.getField2())
                .field3(piaAnnex2Step3Avro.getField3())
                .field4(piaAnnex2Step3Avro.getField4())

                .build();
    }

    private PIAAnnex2Step4JaxRs piaAnnex2Step4JaxRsFromAvro(PIAAnnex2Step4Avro piaAnnex2Step4Avro) {

        return PIAAnnex2Step4JaxRs.builder()
                .field1(piaAnnex2Step4Avro.getField1())
                .field2(piaAnnex2Step4Avro.getField2())
                .field3(piaAnnex2Step4Avro.getField3())
                .field4(piaAnnex2Step4Avro.getField4())

                .build();


    }

    private PIAAnnex2Step5JaxRs piaAnnex2Step5JaxRsFromAvro(PIAAnnex2Step5Avro piaAnnex2Step5Avro) {
        return PIAAnnex2Step5JaxRs.builder()
                .field1(piaAnnex2Step5Avro.getField1())
                .field2(piaAnnex2Step5Avro.getField2())
                .field3(piaAnnex2Step5Avro.getField3())
                .build();
    }

    private PIAAnnex2Step6JaxRs piaAnnex2Step6JaxRsFromAvro(PIAAnnex2Step6Avro piaAnnex2Step6Avro) {
        return PIAAnnex2Step6JaxRs.builder()
                .field1(piaAnnex2Step6Avro.getField1())
                .field2(piaAnnex2Step6Avro.getField2())
                .field3(piaAnnex2Step6Avro.getField3())
                .build();
    }

    private PIAAnnex2Step3Avro piaAnnex2Step3JaxRsFromJaxRs(PIAAnnex2Step3JaxRs piaAnnex2Step3JaxRs) {

        return PIAAnnex2Step3Avro.newBuilder()
                .setField1(piaAnnex2Step3JaxRs.getField1())
                .setField2(piaAnnex2Step3JaxRs.getField2())
                .setField3(piaAnnex2Step3JaxRs.getField3())
                .setField4(piaAnnex2Step3JaxRs.getField4())

                .build();
    }

    private PIAAnnex2Step4Avro piaAnnex2Step4JaxRsFromJaxRs(PIAAnnex2Step4JaxRs piaAnnex2Step4JaxRs) {

        return PIAAnnex2Step4Avro.newBuilder()
                .setField1(piaAnnex2Step4JaxRs.getField1())
                .setField2(piaAnnex2Step4JaxRs.getField2())
                .setField3(piaAnnex2Step4JaxRs.getField3())
                .setField4(piaAnnex2Step4JaxRs.getField4())

                .build();


    }

    private PIAAnnex2Step5Avro piaAnnex2Step5JaxRsFromJaxRs(PIAAnnex2Step5JaxRs piaAnnex2Step5JaxRs) {
        return PIAAnnex2Step5Avro.newBuilder()
                .setField1(piaAnnex2Step5JaxRs.getField1())
                .setField2(piaAnnex2Step5JaxRs.getField2())
                .setField3(piaAnnex2Step5JaxRs.getField3())
                .build();
    }

    private PIAAnnex2Step6Avro piaAnnex2Step6JaxRsFromJaxRs(PIAAnnex2Step6JaxRs piaAnnex2Step6JaxRs) {
        return PIAAnnex2Step6Avro.newBuilder()
                .setField1(piaAnnex2Step6JaxRs.getField1())
                .setField2(piaAnnex2Step6JaxRs.getField2())
                .setField3(piaAnnex2Step6JaxRs.getField3())
                .build();

    }

    private PIAAnnex2Avro piaAnnex2FromJaxRs(PIAAnnex2JaxRs annex2) {

        return PIAAnnex2Avro.newBuilder()
                .setStep1(annex2.getStep1())
                .setStep2Part1(annex2.getStep2Part1())
                .setStep2Part2(annex2.getStep2Part2())
                .setStep3(piaAnnex2Step3JaxRsFromJaxRs(annex2.getStep3()))
                .setStep4(piaAnnex2Step4JaxRsFromJaxRs(annex2.getStep4()))
                .setStep5(piaAnnex2Step5JaxRsFromJaxRs(annex2.getStep5()))
                .setStep6(piaAnnex2Step6JaxRsFromJaxRs(annex2.getStep6()))
                .build();
    }

    private PIAAnnex3Avro piaAnnex3FromJaxRs(PIAAnnex3JaxRs annex3) {

        return PIAAnnex3Avro.newBuilder()
                .setPrinciple1(piaAnnex3Principle1JaxRsFromJaxRs(annex3.getPrinciple1()))
                .setPrinciple2(piaAnnex3Principle2JaxRsFromJaxRs(annex3.getPrinciple2()))
                .setPrinciple3(piaAnnex3Principle3JaxRsFromJaxRs(annex3.getPrinciple3()))
                .setPrinciple4(piaAnnex3Principle4JaxRsFromJaxRs(annex3.getPrinciple4()))
                .setPrinciple5(piaAnnex3Principle5JaxRsFromJaxRs(annex3.getPrinciple5()))
                .setPrinciple6(piaAnnex3Principle6JaxRsFromJaxRs(annex3.getPrinciple6()))
                .setPrinciple7(piaAnnex3Principle7JaxRsFromJaxRs(annex3.getPrinciple7()))
                .setPrinciple8(piaAnnex3Principle8JaxRsFromJaxRs(annex3.getPrinciple8()))

                .build();
    }

    private PIAAnnex3Principle8Avro piaAnnex3Principle8JaxRsFromJaxRs(PIAAnnex3Principle8JaxRs principle8) {
        return PIAAnnex3Principle8Avro.newBuilder()
                .setQuestion1(principle8.getQuestion1())
                .setQuestion2(principle8.getQuestion2())


                .build();
    }

    private PIAAnnex3Principle7Avro piaAnnex3Principle7JaxRsFromJaxRs(PIAAnnex3Principle7JaxRs principle7) {

        return PIAAnnex3Principle7Avro.newBuilder()
                .setQuestion1(principle7.getQuestion1())
                .setQuestion2(principle7.getQuestion2())


                .build();
    }

    private PIAAnnex3Principle6Avro piaAnnex3Principle6JaxRsFromJaxRs(PIAAnnex3Principle6JaxRs principle6) {
        return PIAAnnex3Principle6Avro.newBuilder()
                .setQuestion1(principle6.getQuestion1())
                .setQuestion2(principle6.getQuestion2())


                .build();
    }

    private PIAAnnex3Principle5Avro piaAnnex3Principle5JaxRsFromJaxRs(PIAAnnex3Principle5JaxRs principle5) {
        return PIAAnnex3Principle5Avro.newBuilder()
                .setQuestion1(principle5.getQuestion1())
                .setQuestion2(principle5.getQuestion2())

                .build();
    }

    private PIAAnnex3Principle4Avro piaAnnex3Principle4JaxRsFromJaxRs(PIAAnnex3Principle4JaxRs principle4) {
        return PIAAnnex3Principle4Avro.newBuilder()
                .setQuestion1(principle4.getQuestion1())
                .setQuestion2(principle4.getQuestion2())

                .build();
    }

    private PIAAnnex3Principle3Avro piaAnnex3Principle3JaxRsFromJaxRs(PIAAnnex3Principle3JaxRs principle3) {
        return PIAAnnex3Principle3Avro.newBuilder()
                .setQuestion1(principle3.getQuestion1())
                .setQuestion2(principle3.getQuestion2())
                .build();
    }

    private PIAAnnex3Principle2Avro piaAnnex3Principle2JaxRsFromJaxRs(PIAAnnex3Principle2JaxRs principle2) {
        return PIAAnnex3Principle2Avro.newBuilder()
                .setQuestion1(principle2.getQuestion1())
                .setQuestion2(principle2.getQuestion2())
                .build();
    }

    private PIAAnnex3Principle1Avro piaAnnex3Principle1JaxRsFromJaxRs(PIAAnnex3Principle1JaxRs principle1) {
        return PIAAnnex3Principle1Avro.newBuilder()
                .setQuestion1(principle1.getQuestion1())
                .setQuestion2(principle1.getQuestion2())
                .setQuestion3(principle1.getQuestion3())
                .setQuestion4(principle1.getQuestion4())
                .setQuestion5(principle1.getQuestion5())
                .setQuestion6(principle1.getQuestion6())
                .setQuestion7(principle1.getQuestion7())
                .setQuestion8(principle1.getQuestion8())
                .setQuestion9(principle1.getQuestion9())
                .build();
    }


    private PIACreate fromJaxRs(PIACreateJaxRs customerProfileCreateJaxRs) {

        final ImmutableByteList serialisedDocument = piaAvprImpl.getSerialisedDocument(piaDocumentFromJaxRs(customerProfileCreateJaxRs.getDocument()));
        return PIACreate.builder()
                .subjectProfileId(Long.valueOf(customerProfileCreateJaxRs.getSubjectProfileId()))
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
                .subjectProfileId(Long.valueOf(customerProfileUpdateJaxRs.getSubjectProfileId()))
                .id(Long.valueOf(customerProfileUpdateJaxRs.getId()))
                .lastVersion(Long.valueOf(customerProfileUpdateJaxRs.getLastVersion()))
                .document(piaAvprImpl.getSerialisedDocument(piaDocumentFromJaxRs(customerProfileUpdateJaxRs.getDocument())))
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
