package pias.backend.id.tests.web;

import org.apache.avro.Protocol;
import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Maps;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.*;
import pias.backend.*;
import pias.backend.avro.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AvroTest {
  @Rule public WebInstance webInstance = new WebInstance();

  private CustomerProfileAvpr customerProfileAvpr;
  private SubjectProfileAvpr subjectProfileAvpr;
  private PIAAvpr privacyImpactAssessmentAnnexOneAvpr;
  private PIAAvpr piaAvpr;
  private HttpTransceiver httpTransceiver;

  @Before
  public void setup() throws IOException {
    this.customerProfileAvpr =
        createClient(CustomerProfileAvpr.PROTOCOL, CustomerProfileAvpr.class);
    this.subjectProfileAvpr = createClient(SubjectProfileAvpr.PROTOCOL, SubjectProfileAvpr.class);
    this.privacyImpactAssessmentAnnexOneAvpr = createClient(PIAAvpr.PROTOCOL, PIAAvpr.class);
    this.piaAvpr = createClient(PIAAvpr.PROTOCOL, PIAAvpr.class);
  }

  private <T> T createClient(Protocol protocol, Class<T> iface) throws IOException {
    final URI url =
        webInstance.webClient().getUrlHelper().toBuilder()
            .path(
                Optional.of(
                    String.format("/avpr/%s.%s", protocol.getNamespace(), protocol.getName())))
            .build()
            .getUrl();
    httpTransceiver = new HttpTransceiver(url.toURL());
    return SpecificRequestor.getClient(iface, httpTransceiver);
  }

  @After
  public void shutdown() {
    try {
      this.httpTransceiver.close();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  @Test
  public void testCustomerProfileClient() {
    final CustomerProfileAvro customerProfileAvro =
        createCustomerProfile("testEmail", "testExternalLegalName");
    Assert.assertThat(
        customerProfileAvro,
        IsEqual.equalTo(customerProfileAvpr.avroReadCustomerProfile(customerProfileAvro.getId())));
    final CustomerProfileAvro customerProfileAvro1 =
        customerProfileAvpr.avroUpdateCustomerProfile(
            CustomerProfileUpdateAvro.newBuilder()
                .setId(customerProfileAvro.getId())
                .setLastVersion(customerProfileAvro.getVersion())
                .setExternalEmail("nextEmail")
                .setExternalLegalName("nextExternalLegalName")
                .build());
    Assert.assertThat(
        customerProfileAvro1,
        IsEqual.equalTo(customerProfileAvpr.avroReadCustomerProfile(customerProfileAvro.getId())));
    final CustomerProfileAvro customerProfileAvro2 =
        customerProfileAvpr.avroReadVersionedCustomerProfile("1", "1");
    Assert.assertThat(customerProfileAvro1, IsEqual.equalTo(customerProfileAvro2));
  }

  private CustomerProfileAvro createCustomerProfile(
      String testEmail, String testExternalLegalName) {
    final CustomerProfileCreateAvro customerProfileCreateAvro =
        CustomerProfileCreateAvro.newBuilder()
            .setExternalEmail(testEmail)
            .setExternalLegalName(testExternalLegalName)
            .build();
    return customerProfileAvpr.avroCreateCustomerProfile(customerProfileCreateAvro);
  }

  @Test
  public void testSubjectProfileClient() {

    final SubjectProfileAvro subjectProfileAvro = createSubjectProfile();
    Assert.assertThat(
        subjectProfileAvro,
        IsEqual.equalTo(subjectProfileAvpr.avroReadSubjectProfile(subjectProfileAvro.getId())));
    final SubjectProfileAvro subjectProfileAvro1 =
        subjectProfileAvpr.avroUpdateSubjectProfile(
            SubjectProfileUpdateAvro.newBuilder()
                .setId(subjectProfileAvro.getId())
                .setLastVersion(subjectProfileAvro.getVersion())
                .setExternalSubjectReference("testSubjectReference")
                .setCustomerProfileId(subjectProfileAvro.getCustomerProfileId())
                .setExternalSubjectName("testSubjectName")
                .build());
    Assert.assertThat(
        subjectProfileAvro1,
        IsEqual.equalTo(subjectProfileAvpr.avroReadSubjectProfile(subjectProfileAvro.getId())));
    final SubjectProfileAvro subjectProfileAvro2 =
        subjectProfileAvpr.avroReadVersionedSubjectProfile("1", "1");
    Assert.assertThat(subjectProfileAvro1, IsEqual.equalTo(subjectProfileAvro2));
    List<SubjectProfileAvro> subjectProfileAvros =
        subjectProfileAvpr.avroReadSubjectProfilesForCustomerProfile(
            subjectProfileAvro.getCustomerProfileId());
    Assert.assertThat(
        Lists.immutable.ofAll(subjectProfileAvros),
        IsEqual.equalTo(Lists.immutable.of(subjectProfileAvro2)));
  }

  private SubjectProfileAvro createSubjectProfile() {
    final CustomerProfileAvro customerProfile =
        createCustomerProfile("testSubjectEmail", "testSubjectExternalLegalName");
    final SubjectProfileCreateAvro customerProfileCreateAvro =
        SubjectProfileCreateAvro.newBuilder()
            .setExternalSubjectReference("testSubjectReference")
            .setCustomerProfileId(customerProfile.getId())
            .setExternalSubjectName("testSubjectName")
            .build();

    return subjectProfileAvpr.avroCreateSubjectProfile(customerProfileCreateAvro);
  }

  private PIACreateAvro createPIA() {

    final SubjectProfileAvro subjectProfileAvro = createSubjectProfile();
    final PIADocumentAvro document = createTestDocument();
    PIADocumentAvro piaDocumentAvro = document;
    return PIACreateAvro.newBuilder()
        .setDocument(piaDocumentAvro)
        .setSubjectProfileId(subjectProfileAvro.getId())
        .build();
  }

  private PIADocumentAvro createTestDocument() {
    return PIADocumentAvro.newBuilder()
        .setAnnex1(
            PIAAnnex1Avro.newBuilder()
                .setQuestion1("pia_annex1_question1")
                .setQuestion2("pia_annex1_question2")
                .setQuestion3("pia_annex1_question3")
                .setQuestion4("pia_annex1_question4")
                .setQuestion5("pia_annex1_question5")
                .setQuestion6("pia_annex1_question6")
                .setQuestion7("pia_annex1_question7")
                .build())
        .setAnnex2(
            PIAAnnex2Avro.newBuilder()
                .setStep1("pia_annex2_step1")
                .setStep2Part1("pia_annex2_step2_part1")
                .setStep2Part2("pia_annex2_step2_part2")
                .setStep3(
                    PIAAnnex2Step3Avro.newBuilder()
                        .setField1("pia_annex2_step3_field1")
                        .setField2("pia_annex2_step3_field2")
                        .setField3("pia_annex2_step3_field3")
                        .setField4("pia_annex2_step3_field4")
                        .build())
                .setStep4(
                    PIAAnnex2Step4Avro.newBuilder()
                        .setField1("pia_annex2_step4_field1")
                        .setField2("pia_annex2_step4_field2")
                        .setField3("pia_annex2_step4_field3")
                        .setField4("pia_annex2_step4_field4")
                        .build())
                .setStep5(
                    PIAAnnex2Step5Avro.newBuilder()
                        .setField1("pia_annex2_step5_field1")
                        .setField2("pia_annex2_step5_field2")
                        .setField3("pia_annex2_step5_field3")
                        .build())
                .setStep6(
                    PIAAnnex2Step6Avro.newBuilder()
                        .setField1("pia_annex2_step6_field1")
                        .setField2("pia_annex2_step6_field2")
                        .setField3("pia_annex2_step6_field3")
                        .build())
                .build())
        .setAnnex3(
            PIAAnnex3Avro.newBuilder()
                .setPrinciple1(
                    PIAAnnex3Principle1Avro.newBuilder()
                        .setQuestion1("pia_annex3_principle1_question1")
                        .setQuestion2("pia_annex3_principle1_question2")
                        .setQuestion3("pia_annex3_principle1_question3")
                        .setQuestion4("pia_annex3_principle1_question4")
                        .setQuestion5("pia_annex3_principle1_question5")
                        .setQuestion6("pia_annex3_principle1_question6")
                        .setQuestion7("pia_annex3_principle1_question7")
                        .setQuestion8("pia_annex3_principle1_question8")
                        .setQuestion9("pia_annex3_principle1_question9")
                        .build())
                .setPrinciple2(
                    PIAAnnex3Principle2Avro.newBuilder()
                        .setQuestion1("pia_annex3_principle2_question1")
                        .setQuestion2("pia_annex3_principle2_question2")
                        .build())
                .setPrinciple3(
                    PIAAnnex3Principle3Avro.newBuilder()
                        .setQuestion1("pia_annex3_principle3_question1")
                        .setQuestion2("pia_annex3_principle3_question2")
                        .build())
                .setPrinciple4(
                    PIAAnnex3Principle4Avro.newBuilder()
                        .setQuestion1("pia_annex3_principle4_question1")
                        .setQuestion2("pia_annex3_principle4_question2")
                        .build())
                .setPrinciple5(
                    PIAAnnex3Principle5Avro.newBuilder()
                        .setQuestion1("pia_annex3_principle5_question1")
                        .setQuestion2("pia_annex3_principle5_question2")
                        .build())
                .setPrinciple6(
                    PIAAnnex3Principle6Avro.newBuilder()
                        .setQuestion1("pia_annex3_principle6_question1")
                        .setQuestion2("pia_annex3_principle6_question2")
                        .build())
                .setPrinciple7(
                    PIAAnnex3Principle7Avro.newBuilder()
                        .setQuestion1("pia_annex3_principle7_question1")
                        .setQuestion2("pia_annex3_principle7_question2")
                        .build())
                .setPrinciple8(
                    PIAAnnex3Principle8Avro.newBuilder()
                        .setQuestion1("pia_annex3_principle8_question1")
                        .setQuestion2("pia_annex3_principle8_question2")
                        .build())
                .build())
        .build();
  }

  @Test
  public void testPIAClient() {
    final PIACreateAvro piaCreateAvro = createPIA();
    PIADocumentAvro document = piaCreateAvro.getDocument();
    final PIAAvro piaAvro = piaAvpr.avroCreatePIA(piaCreateAvro);
    Assert.assertThat(piaAvro, IsEqual.equalTo(piaAvpr.avroReadPIA(piaAvro.getId())));
    final PIAAvro updatePiaAvro =
        piaAvpr.avroUpdatePIA(
            PIAUpdateAvro.newBuilder()
                .setId(piaAvro.getId())
                .setLastVersion(piaAvro.getVersion())
                .setSubjectProfileId(piaCreateAvro.getSubjectProfileId())
                .setDocument(
                    PIADocumentAvro.newBuilder()
                        .setAnnex1(document.getAnnex1())
                        .setAnnex2(document.getAnnex2())
                        .setAnnex3(
                            PIAAnnex3Avro.newBuilder(document.getAnnex3())
                                .setPrinciple1(
                                    PIAAnnex3Principle1Avro.newBuilder(
                                            document.getAnnex3().getPrinciple1())
                                        .setQuestion1("a_different_question")
                                        .build())
                                .build())
                        .build())
                .build());
    Assert.assertThat(updatePiaAvro, IsEqual.equalTo(piaAvpr.avroReadPIA(piaAvro.getId())));
    Assert.assertThat(updatePiaAvro, IsEqual.equalTo(piaAvpr.avroReadVersionedPIA("1", "1")));
    Assert.assertThat(updatePiaAvro, Matchers.not(IsEqual.equalTo(piaAvro)));
  }

  @Test
  public void testPrivacyImpactAssessmentAnnexOneClient() {
    final SubjectProfileAvro subjectProfileAvro = createSubjectProfile();
    ImmutableMap<String, String> iniitalQuesions =
        Maps.immutable.of("testQuestion1", "testAnswer1");
    Map<String, String> value = iniitalQuesions.toMap();
    final PIACreateAvro create =
        PIACreateAvro.newBuilder()
            .setSubjectProfileId(subjectProfileAvro.getId())
            .setDocument(createTestDocument())
            .build();
    PIAAvro firstRest = privacyImpactAssessmentAnnexOneAvpr.avroCreatePIA(create);
    Assert.assertThat(
        firstRest,
        IsEqual.equalTo(privacyImpactAssessmentAnnexOneAvpr.avroReadPIA(firstRest.getId())));
    final PIADocumentAvro testDocument = createTestDocument();
    PIAUpdateAvro build =
        PIAUpdateAvro.newBuilder()
            .setId(firstRest.getId())
            .setLastVersion(firstRest.getVersion())
            .setDocument(
                PIADocumentAvro.newBuilder(testDocument)
                    .setAnnex1(
                        PIAAnnex1Avro.newBuilder(testDocument.getAnnex1())
                            .setQuestion1("SomethingElse")
                            .build())
                    .build())
            .setSubjectProfileId(String.valueOf(firstRest.getSubjectProfileId()))
            .build();
    final PIAAvro updated = privacyImpactAssessmentAnnexOneAvpr.avroUpdatePIA(build);
    Assert.assertThat(
        updated,
        IsEqual.equalTo(privacyImpactAssessmentAnnexOneAvpr.avroReadPIA(firstRest.getId())));
    final PIAAvro privacyImpactAssessmentAnnexOneAvro =
        privacyImpactAssessmentAnnexOneAvpr.avroReadVersionedPIA("1", "1");
    Assert.assertThat(updated, IsEqual.equalTo(privacyImpactAssessmentAnnexOneAvro));
  }
}
