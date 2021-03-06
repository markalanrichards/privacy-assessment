import org.apache.avro.AvroRemoteException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
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
import pias.backend.PIAAvpr;
import pias.backend.PIAAvro;
import pias.backend.PIACreateAvro;
import pias.backend.PIADocumentAvro;
import pias.backend.PIAUpdateAvro;

public class PIATest {

    public static final PIADocumentAvro RANDOM_DOCUMENT() {
        return PIADocumentAvro.newBuilder()
                .setAnnex1(PIAAnnex1Avro.newBuilder()
                        .setQuestion1(AvroServiceExternalResource.RANDOM_UTF8())
                        .setQuestion2(AvroServiceExternalResource.RANDOM_UTF8())
                        .setQuestion3(AvroServiceExternalResource.RANDOM_UTF8())
                        .setQuestion4(AvroServiceExternalResource.RANDOM_UTF8())
                        .setQuestion5(AvroServiceExternalResource.RANDOM_UTF8())
                        .setQuestion6(AvroServiceExternalResource.RANDOM_UTF8())
                        .setQuestion7(AvroServiceExternalResource.RANDOM_UTF8())
                        .build())
                .setAnnex2(PIAAnnex2Avro.newBuilder()
                        .setStep1(AvroServiceExternalResource.RANDOM_UTF8())
                        .setStep2Part1(AvroServiceExternalResource.RANDOM_UTF8())
                        .setStep2Part2(AvroServiceExternalResource.RANDOM_UTF8())
                        .setStep3(PIAAnnex2Step3Avro.newBuilder()
                                .setField1(AvroServiceExternalResource.RANDOM_UTF8())
                                .setField2(AvroServiceExternalResource.RANDOM_UTF8())
                                .setField3(AvroServiceExternalResource.RANDOM_UTF8())
                                .setField4(AvroServiceExternalResource.RANDOM_UTF8())
                                .build())
                        .setStep4(PIAAnnex2Step4Avro.newBuilder()
                                .setField1(AvroServiceExternalResource.RANDOM_UTF8())
                                .setField2(AvroServiceExternalResource.RANDOM_UTF8())
                                .setField3(AvroServiceExternalResource.RANDOM_UTF8())
                                .setField4(AvroServiceExternalResource.RANDOM_UTF8())
                                .build())
                        .setStep5(PIAAnnex2Step5Avro.newBuilder()
                                .setField1(AvroServiceExternalResource.RANDOM_UTF8())
                                .setField2(AvroServiceExternalResource.RANDOM_UTF8())
                                .setField3(AvroServiceExternalResource.RANDOM_UTF8())
                                .build())
                        .setStep6(PIAAnnex2Step6Avro.newBuilder()
                                .setField1(AvroServiceExternalResource.RANDOM_UTF8())
                                .setField2(AvroServiceExternalResource.RANDOM_UTF8())
                                .setField3(AvroServiceExternalResource.RANDOM_UTF8())
                                .build())
                        .build())
                .setAnnex3(PIAAnnex3Avro.newBuilder()
                        .setPrinciple1(PIAAnnex3Principle1Avro.newBuilder()
                                .setQuestion1(AvroServiceExternalResource.RANDOM_UTF8())
                                .setQuestion2(AvroServiceExternalResource.RANDOM_UTF8())
                                .setQuestion3(AvroServiceExternalResource.RANDOM_UTF8())
                                .setQuestion4(AvroServiceExternalResource.RANDOM_UTF8())
                                .setQuestion5(AvroServiceExternalResource.RANDOM_UTF8())
                                .setQuestion6(AvroServiceExternalResource.RANDOM_UTF8())
                                .setQuestion7(AvroServiceExternalResource.RANDOM_UTF8())
                                .setQuestion8(AvroServiceExternalResource.RANDOM_UTF8())
                                .setQuestion9(AvroServiceExternalResource.RANDOM_UTF8())
                                .build())
                        .setPrinciple2(PIAAnnex3Principle2Avro.newBuilder()
                                .setQuestion1(AvroServiceExternalResource.RANDOM_UTF8())
                                .setQuestion2(AvroServiceExternalResource.RANDOM_UTF8())
                                .build())
                        .setPrinciple3(PIAAnnex3Principle3Avro.newBuilder()
                                .setQuestion1(AvroServiceExternalResource.RANDOM_UTF8())
                                .setQuestion2(AvroServiceExternalResource.RANDOM_UTF8())
                                .build())
                        .setPrinciple4(PIAAnnex3Principle4Avro.newBuilder()
                                .setQuestion1(AvroServiceExternalResource.RANDOM_UTF8())
                                .setQuestion2(AvroServiceExternalResource.RANDOM_UTF8())
                                .build())
                        .setPrinciple5(PIAAnnex3Principle5Avro.newBuilder()
                                .setQuestion1(AvroServiceExternalResource.RANDOM_UTF8())
                                .setQuestion2(AvroServiceExternalResource.RANDOM_UTF8())
                                .build())
                        .setPrinciple6(PIAAnnex3Principle6Avro.newBuilder()
                                .setQuestion1(AvroServiceExternalResource.RANDOM_UTF8())
                                .setQuestion2(AvroServiceExternalResource.RANDOM_UTF8())
                                .build())
                        .setPrinciple7(PIAAnnex3Principle7Avro.newBuilder()
                                .setQuestion1(AvroServiceExternalResource.RANDOM_UTF8())
                                .setQuestion2(AvroServiceExternalResource.RANDOM_UTF8())
                                .build())
                        .setPrinciple8(PIAAnnex3Principle8Avro.newBuilder()
                                .setQuestion1(AvroServiceExternalResource.RANDOM_UTF8())
                                .setQuestion2(AvroServiceExternalResource.RANDOM_UTF8())
                                .build())
                        .build())
                .build();
    }


    public static PIAAvro RANDOM_PIA() {
        return PIAAvro.newBuilder()
                .setEpoch(AvroServiceExternalResource.RANDOM_UTF8())
                .setId(AvroServiceExternalResource.RANDOM_UTF8())
                .setVersion(AvroServiceExternalResource.RANDOM_UTF8())
                .setSubjectProfileId(AvroServiceExternalResource.RANDOM_UTF8())
                .setDocument(RANDOM_DOCUMENT())
                .build();
    }


    public static PIACreateAvro RANDOM_PIA_CREATE() {
        return PIACreateAvro.newBuilder()
                .setSubjectProfileId(AvroServiceExternalResource.RANDOM_UTF8())
                .setDocument(RANDOM_DOCUMENT())
                .build();
    }

    public static PIAUpdateAvro RANDOM_PIA_UPDATE() {
        return PIAUpdateAvro.newBuilder()
                .setId(AvroServiceExternalResource.RANDOM_UTF8())
                .setLastVersion(AvroServiceExternalResource.RANDOM_UTF8())
                .setSubjectProfileId(AvroServiceExternalResource.RANDOM_UTF8())
                .setDocument(RANDOM_DOCUMENT())
                .build();
    }

    @Rule
    public AvroServiceExternalResource<PIAAvpr> privacyImpactAssessmentAnnexOneServiceAvroServiceExternalResource = new AvroServiceExternalResource<>(PIAAvpr.class);


    @Test
    public void testCreate() throws AvroRemoteException {
        PIACreateAvro privacyImpactAssessmentAnnexOneRequest = RANDOM_PIA_CREATE();
        PIAAvro expectedPrivacyImpactAssessmentAnnexOne = RANDOM_PIA();
        Mockito.doReturn(expectedPrivacyImpactAssessmentAnnexOne).when(privacyImpactAssessmentAnnexOneServiceAvroServiceExternalResource.getService()).avroCreatePIA(privacyImpactAssessmentAnnexOneRequest);
        PIAAvro actualPrivacyImpactAssessmentAnnexOne = privacyImpactAssessmentAnnexOneServiceAvroServiceExternalResource.getClient().avroCreatePIA(privacyImpactAssessmentAnnexOneRequest);
        Assert.assertThat(actualPrivacyImpactAssessmentAnnexOne, CoreMatchers.equalTo(expectedPrivacyImpactAssessmentAnnexOne));
    }

    @Test
    public void testUpdate() throws AvroRemoteException {
        PIAUpdateAvro privacyImpactAssessmentAnnexOneUpdate = RANDOM_PIA_UPDATE();
        PIAAvro expectedUpdate = RANDOM_PIA();
        Mockito.doReturn(expectedUpdate).when(privacyImpactAssessmentAnnexOneServiceAvroServiceExternalResource.getService()).avroUpdatePIA(privacyImpactAssessmentAnnexOneUpdate);
        PIAAvro actualPrivacyImpactAssessmentAnnexOne = privacyImpactAssessmentAnnexOneServiceAvroServiceExternalResource.getClient().avroUpdatePIA(privacyImpactAssessmentAnnexOneUpdate);
        Assert.assertThat(actualPrivacyImpactAssessmentAnnexOne, CoreMatchers.equalTo(expectedUpdate));
    }

    @Test
    public void testRead() throws AvroRemoteException {

        final PIAAvro expectedUpdate = RANDOM_PIA();
        final String id = expectedUpdate.getId();
        Mockito.doReturn(expectedUpdate).when(privacyImpactAssessmentAnnexOneServiceAvroServiceExternalResource.getService()).avroReadPIA(id);
        PIAAvro actualPrivacyImpactAssessmentAnnexOne = privacyImpactAssessmentAnnexOneServiceAvroServiceExternalResource.getClient().avroReadPIA(id);
        Assert.assertThat(actualPrivacyImpactAssessmentAnnexOne, CoreMatchers.equalTo(expectedUpdate));
    }

}
