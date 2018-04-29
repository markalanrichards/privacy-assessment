package pias.backend.id.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import pias.backend.flyway.FlywayConfig;
import pias.backend.flyway.FlywayJdbcConfig;
import pias.backend.flyway.postgres.PostgresFlywayManaged;
import pias.backend.flyway.postgres.PostgresMigrator;
import pias.backend.id.server.entity.PrivacyConfiguration;
import pias.backend.id.server.entity.JdbcConfiguration;
import pias.backend.id.server.jaxrs.CustomerProfileServiceInterfaceJaxRs;
import pias.backend.id.server.jaxrs.PIAServiceInterfaceJaxRs;
import pias.backend.id.server.jaxrs.SubjectProfileServiceInterfaceJaxRs;
import pias.backend.id.server.jaxrs.model.CustomerProfileCreateJaxRs;
import pias.backend.id.server.jaxrs.model.CustomerProfileJaxRs;
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
import pias.backend.id.server.jaxrs.model.SubjectProfileCreateJaxRs;
import pias.backend.id.server.jaxrs.model.SubjectProfileJaxRs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PostgresPrivacyServerTest {
    PostgreSQLContainer postgreSQLContainer;
    private Connection serverConnection;
    private PrivacyServer privacyServer;
    private PostgresFlywayManaged flywayManaged;
    ObjectMapper objectMapper = new ObjectMapper();
    JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider(objectMapper);
    CustomerProfileServiceInterfaceJaxRs customerProfileJaxrs;
    SubjectProfileServiceInterfaceJaxRs subjectProfileServiceInterfaceJaxRs;
    PIAServiceInterfaceJaxRs piaInterfaceJaxRs;


    @Before
    public void setup() throws Exception {
        postgreSQLContainer = new PostgreSQLContainer();

        postgreSQLContainer.start();
        final String password = postgreSQLContainer.getPassword();
        final String username = postgreSQLContainer.getUsername();
        final String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        serverConnection = DriverManager.getConnection(jdbcUrl, username, password);
        final FlywayJdbcConfig flywayJdbcConfig;
        flywayJdbcConfig = FlywayJdbcConfig.builder()
                .jdbcUrl(jdbcUrl)
                .user(username)
                .password(password)
                .build();
        flywayManaged = new PostgresFlywayManaged(FlywayConfig.builder().flywayJdbcConfig(flywayJdbcConfig).classForPackage(PostgresMigrator.class).build());
        flywayManaged.migrate();
        privacyServer = new PrivacyServer(PrivacyConfiguration.builder()
                .hostname("127.0.0.1")
                .port(0)
                .database("postgres")
                .serverJdbcConfiguration(JdbcConfiguration.builder()
                        .jdbcUrl(jdbcUrl)
                        .jdbcUsername(username)
                        .jdbcPassword(password)
                        .build())

                .build());
        customerProfileJaxrs = JAXRSClientFactory.create(String.format("http://127.0.0.1:%s/rest", privacyServer.getPort()), CustomerProfileServiceInterfaceJaxRs.class, Lists.immutable.of(jacksonJsonProvider).castToList());
        subjectProfileServiceInterfaceJaxRs = JAXRSClientFactory.create(String.format("http://127.0.0.1:%s/rest", privacyServer.getPort()), SubjectProfileServiceInterfaceJaxRs.class, Lists.immutable.of(jacksonJsonProvider).castToList());
        piaInterfaceJaxRs = JAXRSClientFactory.create(String.format("http://127.0.0.1:%s/rest", privacyServer.getPort()), PIAServiceInterfaceJaxRs.class, Lists.immutable.of(jacksonJsonProvider).castToList());
    }

    @After
    public void stop() throws Exception {
        privacyServer.close();
        flywayManaged.clean();
        serverConnection.close();
        postgreSQLContainer.stop();
        postgreSQLContainer.close();
    }

    @Test
    public void testPing() throws Exception {
        final AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
        final CompletableFuture<Response> responseCompletableFuture = asyncHttpClient
                .prepareGet(String.format("http://127.0.0.1:%s/ping", privacyServer.getPort())).execute().toCompletableFuture();
        final Response response = responseCompletableFuture.get();
        assertThat(response.getStatusCode(), equalTo(200));
        assertThat(response.getResponseBody(), equalTo("pong"));
    }

    @Test
    public void testCustomerProfileService() throws Exception {

        String testExternalEmail = "testExternalEmail";
        String testExternalLegalEntity = "testExternalLegalEntity";

        CustomerProfileJaxRs customerProfileJaxRs = createCustomerProfile(testExternalEmail, testExternalLegalEntity);
        assertThat(customerProfileJaxRs.getExternalEmail(), equalTo(testExternalEmail));
        assertThat(customerProfileJaxRs.getExternalLegalEntity(), equalTo(testExternalLegalEntity));
    }

    private CustomerProfileJaxRs createCustomerProfile(String testExternalEmail, String testExternalLegalEntity) {
        CustomerProfileCreateJaxRs customerProfileCreateJaxRs = CustomerProfileCreateJaxRs.builder()
                .externalEmail(testExternalEmail)
                .externalLegalEntity(testExternalLegalEntity)
                .build();

        return customerProfileJaxrs.create(customerProfileCreateJaxRs);
    }

    @Test
    public void testSubjectProfileService() throws Exception {
        final CustomerProfileJaxRs customerProfileJaxRs = createCustomerProfile("testSubjectEmail", "testSubjectExternalLegalEntity");
        final String testExternalSubjectName = "testExternalSubjectName";
        final String testExternalSubjectReference = "testExternalSubjectReference";
        final SubjectProfileJaxRs subjectProfileJaxRs = createSubjectProfile(customerProfileJaxRs, testExternalSubjectName, testExternalSubjectReference);

        assertThat(subjectProfileJaxRs.getCustomerProfileId(), equalTo(customerProfileJaxRs.getId()));
        assertThat(subjectProfileJaxRs.getExternalSubjectName(), equalTo(testExternalSubjectName));
        assertThat(subjectProfileJaxRs.getExternalSubjectReference(), equalTo(testExternalSubjectReference));
    }

    private SubjectProfileJaxRs createSubjectProfile(CustomerProfileJaxRs customerProfileJaxRs, String testExternalSubjectName, String testExternalSubjectReference) {
        return subjectProfileServiceInterfaceJaxRs.create(SubjectProfileCreateJaxRs.builder()
                .customerProfileId(customerProfileJaxRs.getId())
                .externalSubjectName(testExternalSubjectName)
                .externalSubjectReference(testExternalSubjectReference)

                .build());
    }

    @Test
    public void testPiasAnnexOneService() throws Exception {

        String testExternalEmail = "testAnnexOneExternalEmail";
        String testExternalLegalEntity = "testAnnexOneExternalLegalEntity";
        final CustomerProfileJaxRs customerProfile = createCustomerProfile(testExternalEmail, testExternalLegalEntity);
        String testExternalSubjectName = "testAnnexOneExternalSubjectName";
        String testExternalSubjectReference = "testAnnexOneExternalSubjectReference";
        final SubjectProfileJaxRs subjectProfileJaxRs = createSubjectProfile(customerProfile, testExternalSubjectName, testExternalSubjectReference);
        String subjectProfileJaxRsId = subjectProfileJaxRs.getId();
        final PIADocumentJaxRs document = PIADocumentJaxRs.builder()
                .annex1(PIAAnnex1JaxRs.builder()
                        .question1("testAnnex1Question1")
                        .question2("testAnnex1Question2")
                        .question3("testAnnex1Question3")
                        .question4("testAnnex1Question4")
                        .question5("testAnnex1Question5")
                        .question6("testAnnex1Question6")
                        .question7("testAnnex1Question7")
                        .build())
                .annex2(PIAAnnex2JaxRs.builder()
                        .step1("testAnnex2Step1")
                        .step2Part1("testAnnex2Step2Part1")
                        .step2Part2("testAnnex2Step2Part1")
                        .step3(PIAAnnex2Step3JaxRs.builder()
                                .field1("testAnnex2Step3Field1")
                                .field2("testAnnex2Step3Field2")
                                .field3("testAnnex2Step3Field3")
                                .field4("testAnnex2Step3Field4")
                                .build())
                        .step4(PIAAnnex2Step4JaxRs.builder()
                                .field1("testAnnex2Step4Field1")
                                .field2("testAnnex2Step4Field2")
                                .field3("testAnnex2Step4Field3")
                                .field4("testAnnex2Step4Field4")
                                .build())
                        .step5(PIAAnnex2Step5JaxRs.builder()
                                .field1("testAnnex2Step5Field1")
                                .field2("testAnnex2Step5Field2")
                                .field3("testAnnex2Step5Field3")
                                .build())
                        .step6(PIAAnnex2Step6JaxRs.builder()
                                .field1("testAnnex2Step6Field1")
                                .field2("testAnnex2Step6Field2")
                                .field3("testAnnex2Step6Field3")
                                .build())
                        .build())
                .annex3(PIAAnnex3JaxRs.builder()
                        .principle1(PIAAnnex3Principle1JaxRs.builder()
                                .question1("testAnnex3Principle1Question1")
                                .question2("testAnnex3Principle1Question2")
                                .question3("testAnnex3Principle1Question4")
                                .question4("testAnnex3Principle1Question4")
                                .question5("testAnnex3Principle1Question5")
                                .question6("testAnnex3Principle1Question6")
                                .question7("testAnnex3Principle1Question7")
                                .question8("testAnnex3Principle1Question8")
                                .question9("testAnnex3Principle1Question9")
                                .build())
                        .principle2(PIAAnnex3Principle2JaxRs.builder()
                                .question1("testAnnex3Principle2Question1")
                                .question2("testAnnex3Principle2Question2")
                                .build())
                        .principle3(PIAAnnex3Principle3JaxRs.builder()
                                .question1("testAnnex3Principle3Question1")
                                .question2("testAnnex3Principle3Question2")
                                .build())
                        .principle4(PIAAnnex3Principle4JaxRs.builder()
                                .question1("testAnnex3Principle4Question1")
                                .question2("testAnnex3Principle4Question2")
                                .build())
                        .principle5(PIAAnnex3Principle5JaxRs.builder()
                                .question1("testAnnex3Principle5Question1")
                                .question2("testAnnex3Principle5Question2")
                                .build())
                        .principle6(PIAAnnex3Principle6JaxRs.builder()
                                .question1("testAnnex3Principle6Question1")
                                .question2("testAnnex3Principle6Question2")
                                .build())
                        .principle7(PIAAnnex3Principle7JaxRs.builder()
                                .question1("testAnnex3Principle7Question1")
                                .question2("testAnnex3Principle7Question2")
                                .build())
                        .principle8(PIAAnnex3Principle8JaxRs.builder()
                                .question1("testAnnex3Principle8Question1")
                                .question2("testAnnex3Principle8Question2")
                                .build())

                        .build())
                .build();
        PIAJaxRs piaAnnexOneJaxRs = piaInterfaceJaxRs.create(PIACreateJaxRs.builder()
                .subjectProfileId(subjectProfileJaxRsId)
                .document(document)
                .build());


        assertThat(piaAnnexOneJaxRs.getDocument(), equalTo(document));
        assertThat(piaAnnexOneJaxRs.getSubjectProfileId(), equalTo(subjectProfileJaxRsId));
    }

}