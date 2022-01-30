package pias.backend;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;

import java.io.IOException;
import org.apache.avro.ipc.Callback;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class PIATest {

  public static final PIADocumentAvro RANDOM_DOCUMENT() {
    return PIADocumentAvro.newBuilder()
        .setAnnex1(
            PIAAnnex1Avro.newBuilder()
                .setQuestion1(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                .setQuestion2(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                .setQuestion3(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                .setQuestion4(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                .setQuestion5(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                .setQuestion6(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                .setQuestion7(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                .build())
        .setAnnex2(
            PIAAnnex2Avro.newBuilder()
                .setStep1(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                .setStep2Part1(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                .setStep2Part2(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                .setStep3(
                    PIAAnnex2Step3Avro.newBuilder()
                        .setField1(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setField2(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setField3(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setField4(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .build())
                .setStep4(
                    PIAAnnex2Step4Avro.newBuilder()
                        .setField1(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setField2(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setField3(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setField4(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .build())
                .setStep5(
                    PIAAnnex2Step5Avro.newBuilder()
                        .setField1(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setField2(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setField3(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .build())
                .setStep6(
                    PIAAnnex2Step6Avro.newBuilder()
                        .setField1(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setField2(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setField3(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .build())
                .build())
        .setAnnex3(
            PIAAnnex3Avro.newBuilder()
                .setPrinciple1(
                    PIAAnnex3Principle1Avro.newBuilder()
                        .setQuestion1(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setQuestion2(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setQuestion3(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setQuestion4(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setQuestion5(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setQuestion6(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setQuestion7(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setQuestion8(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setQuestion9(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .build())
                .setPrinciple2(
                    PIAAnnex3Principle2Avro.newBuilder()
                        .setQuestion1(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setQuestion2(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .build())
                .setPrinciple3(
                    PIAAnnex3Principle3Avro.newBuilder()
                        .setQuestion1(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setQuestion2(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .build())
                .setPrinciple4(
                    PIAAnnex3Principle4Avro.newBuilder()
                        .setQuestion1(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setQuestion2(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .build())
                .setPrinciple5(
                    PIAAnnex3Principle5Avro.newBuilder()
                        .setQuestion1(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setQuestion2(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .build())
                .setPrinciple6(
                    PIAAnnex3Principle6Avro.newBuilder()
                        .setQuestion1(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setQuestion2(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .build())
                .setPrinciple7(
                    PIAAnnex3Principle7Avro.newBuilder()
                        .setQuestion1(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setQuestion2(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .build())
                .setPrinciple8(
                    PIAAnnex3Principle8Avro.newBuilder()
                        .setQuestion1(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .setQuestion2(AsyncAvroServiceExternalResource.RANDOM_UTF8())
                        .build())
                .build())
        .build();
  }

  public static PIAAvro RANDOM_PIA() {
    return PIAAvro.newBuilder()
        .setEpoch(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setId(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setVersion(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setSubjectProfileId(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setDocument(RANDOM_DOCUMENT())
        .build();
  }

  public static PIACreateAvro RANDOM_PIA_CREATE() {
    return PIACreateAvro.newBuilder()
        .setSubjectProfileId(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setDocument(RANDOM_DOCUMENT())
        .build();
  }

  public static PIAUpdateAvro RANDOM_PIA_UPDATE() {
    return PIAUpdateAvro.newBuilder()
        .setId(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setLastVersion(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setSubjectProfileId(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setDocument(RANDOM_DOCUMENT())
        .build();
  }

  @RegisterExtension
  public AsyncAvroServiceExternalResource<PIAAvpr.Callback>
      privacyImpactAssessmentAnnexOneServiceAvroServiceExternalResource =
          new AsyncAvroServiceExternalResource<>(PIAAvpr.Callback.class);

  @Test
  public void testCreate() throws IOException {
    PIACreateAvro privacyImpactAssessmentAnnexOneRequest = RANDOM_PIA_CREATE();
    PIAAvro expectedPrivacyImpactAssessmentAnnexOne = RANDOM_PIA();
    doAnswer(
            invocation -> {
              invocation
                  .<Callback<PIAAvro>>getArgument(1)
                  .handleResult(expectedPrivacyImpactAssessmentAnnexOne);
              return null;
            })
        .when(privacyImpactAssessmentAnnexOneServiceAvroServiceExternalResource.getService())
        .avroCreatePIA(eq(privacyImpactAssessmentAnnexOneRequest), any(Callback.class));

    PIAAvro actualPrivacyImpactAssessmentAnnexOne =
        privacyImpactAssessmentAnnexOneServiceAvroServiceExternalResource
            .getClient()
            .avroCreatePIA(privacyImpactAssessmentAnnexOneRequest);
    MatcherAssert.assertThat(
        actualPrivacyImpactAssessmentAnnexOne,
        CoreMatchers.equalTo(expectedPrivacyImpactAssessmentAnnexOne));
  }

  @Test
  public void testUpdate() throws IOException {
    PIAUpdateAvro privacyImpactAssessmentAnnexOneUpdate = RANDOM_PIA_UPDATE();
    PIAAvro expectedUpdate = RANDOM_PIA();
    doAnswer(
            invocation -> {
              invocation.<Callback<PIAAvro>>getArgument(1).handleResult(expectedUpdate);
              return null;
            })
        .when(privacyImpactAssessmentAnnexOneServiceAvroServiceExternalResource.getService())
        .avroUpdatePIA(eq(privacyImpactAssessmentAnnexOneUpdate), any(Callback.class));
    PIAAvro actualPrivacyImpactAssessmentAnnexOne =
        privacyImpactAssessmentAnnexOneServiceAvroServiceExternalResource
            .getClient()
            .avroUpdatePIA(privacyImpactAssessmentAnnexOneUpdate);
    MatcherAssert.assertThat(
        actualPrivacyImpactAssessmentAnnexOne, CoreMatchers.equalTo(expectedUpdate));
  }

  @Test
  public void testRead() throws IOException {
    final PIAAvro expectedUpdate = RANDOM_PIA();
    final String id = expectedUpdate.getId();
    doAnswer(
            invocation -> {
              invocation.<Callback<PIAAvro>>getArgument(1).handleResult(expectedUpdate);
              return null;
            })
        .when(privacyImpactAssessmentAnnexOneServiceAvroServiceExternalResource.getService())
        .avroReadPIA(eq(id), any(Callback.class));
    ;
    PIAAvro actualPrivacyImpactAssessmentAnnexOne =
        privacyImpactAssessmentAnnexOneServiceAvroServiceExternalResource
            .getClient()
            .avroReadPIA(id);
    MatcherAssert.assertThat(
        actualPrivacyImpactAssessmentAnnexOne, CoreMatchers.equalTo(expectedUpdate));
  }
}
