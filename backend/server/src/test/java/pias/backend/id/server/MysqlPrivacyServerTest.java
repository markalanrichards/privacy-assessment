package pias.backend.id.server;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.CompletableFuture;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.containers.MySQLContainer;
import pias.backend.flyway.FlywayConfig;
import pias.backend.flyway.FlywayJdbcConfig;
import pias.backend.flyway.mysql.MysqlFlywayManaged;
import pias.backend.flyway.mysql.MysqlMigrator;
import pias.backend.id.server.entity.JdbcConfiguration;
import pias.backend.id.server.entity.PrivacyConfiguration;
import pias.backend.id.server.jaxrs.CustomerProfileServiceInterfaceJaxRs;
import pias.backend.id.server.jaxrs.PIAServiceInterfaceJaxRs;
import pias.backend.id.server.jaxrs.SubjectProfileServiceInterfaceJaxRs;
import pias.backend.id.server.jaxrs.model.*;

public class MysqlPrivacyServerTest {
  MySQLContainer mySQLContainer;
  private Connection serverConnection;
  private PrivacyServer privacyServer;
  private MysqlFlywayManaged flywayManaged;
  ObjectMapper objectMapper = new ObjectMapper();
  JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider(objectMapper);
  CustomerProfileServiceInterfaceJaxRs customerProfileJaxrs;
  SubjectProfileServiceInterfaceJaxRs subjectProfileServiceInterfaceJaxRs;
  PIAServiceInterfaceJaxRs piaInterfaceJaxRs;

  @Before
  public void setup() throws Exception {
    mySQLContainer = new MySQLContainer("mysql:8");
    mySQLContainer.start();
    final String password = mySQLContainer.getPassword();
    final String username = mySQLContainer.getUsername();
    final String jdbcUrl = mySQLContainer.getJdbcUrl();
    serverConnection = DriverManager.getConnection(jdbcUrl, username, password);
    final FlywayJdbcConfig flywayJdbcConfig;

    flywayJdbcConfig = new FlywayJdbcConfig(jdbcUrl, username, password, "", 30L);

    flywayManaged = new MysqlFlywayManaged(new FlywayConfig(flywayJdbcConfig, MysqlMigrator.class));
    flywayManaged.migrate();
    final String hostname = "127.0.0.1";
    final int port = 0;
    final String database = "mysql";
    final JdbcConfiguration jdbcConfiguration = new JdbcConfiguration(jdbcUrl, username, password);
    privacyServer =
        new PrivacyServer(new PrivacyConfiguration(port, hostname, jdbcConfiguration, database));
    customerProfileJaxrs =
        JAXRSClientFactory.create(
            String.format("http://127.0.0.1:%s/rest", privacyServer.getPort()),
            CustomerProfileServiceInterfaceJaxRs.class,
            Lists.immutable.of(jacksonJsonProvider).castToList());
    subjectProfileServiceInterfaceJaxRs =
        JAXRSClientFactory.create(
            String.format("http://127.0.0.1:%s/rest", privacyServer.getPort()),
            SubjectProfileServiceInterfaceJaxRs.class,
            Lists.immutable.of(jacksonJsonProvider).castToList());
    piaInterfaceJaxRs =
        JAXRSClientFactory.create(
            String.format("http://127.0.0.1:%s/rest", privacyServer.getPort()),
            PIAServiceInterfaceJaxRs.class,
            Lists.immutable.of(jacksonJsonProvider).castToList());
  }

  @After
  public void stop() throws Exception {
    privacyServer.close();
    flywayManaged.clean();
    serverConnection.close();
    mySQLContainer.stop();
    mySQLContainer.close();
  }

  @Test
  public void testPing() throws Exception {
    final AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
    final CompletableFuture<Response> responseCompletableFuture =
        asyncHttpClient
            .prepareGet(String.format("http://127.0.0.1:%s/ping", privacyServer.getPort()))
            .execute()
            .toCompletableFuture();
    final Response response = responseCompletableFuture.get();
    assertThat(response.getStatusCode(), equalTo(200));
    assertThat(response.getResponseBody(), equalTo("pong"));
  }

  @Test
  public void testCustomerProfileService() throws Exception {

    String testExternalEmail = "testExternalEmail";
    String testExternalLegalEntity = "testExternalLegalEntity";

    CustomerProfileJaxRs customerProfileJaxRs =
        createCustomerProfile(testExternalEmail, testExternalLegalEntity);
    assertThat(customerProfileJaxRs.externalEmail(), equalTo(testExternalEmail));
    assertThat(customerProfileJaxRs.externalLegalEntity(), equalTo(testExternalLegalEntity));
  }

  private CustomerProfileJaxRs createCustomerProfile(
      String testExternalEmail, String testExternalLegalEntity) {
    CustomerProfileCreateJaxRs customerProfileCreateJaxRs =
        new CustomerProfileCreateJaxRs(testExternalEmail, testExternalLegalEntity);

    return customerProfileJaxrs.create(customerProfileCreateJaxRs);
  }

  @Test
  public void testSubjectProfileService() throws Exception {
    final CustomerProfileJaxRs customerProfileJaxRs =
        createCustomerProfile("testSubjectEmail", "testSubjectExternalLegalEntity");
    final String testExternalSubjectName = "testExternalSubjectName";
    final String testExternalSubjectReference = "testExternalSubjectReference";
    final SubjectProfileJaxRs subjectProfileJaxRs =
        createSubjectProfile(
            customerProfileJaxRs, testExternalSubjectName, testExternalSubjectReference);

    assertThat(subjectProfileJaxRs.customerProfileId(), equalTo(customerProfileJaxRs.id()));
    assertThat(subjectProfileJaxRs.externalSubjectName(), equalTo(testExternalSubjectName));
    assertThat(
        subjectProfileJaxRs.externalSubjectReference(), equalTo(testExternalSubjectReference));
  }

  private SubjectProfileJaxRs createSubjectProfile(
      CustomerProfileJaxRs customerProfileJaxRs,
      String testExternalSubjectName,
      String testExternalSubjectReference) {
    return subjectProfileServiceInterfaceJaxRs.create(
        new SubjectProfileCreateJaxRs(
            testExternalSubjectReference, testExternalSubjectName, customerProfileJaxRs.id()));
  }

  @Test
  public void testPiasAnnexOneService() throws Exception {

    String testExternalEmail = "testAnnexOneExternalEmail";
    String testExternalLegalEntity = "testAnnexOneExternalLegalEntity";
    final CustomerProfileJaxRs customerProfile =
        createCustomerProfile(testExternalEmail, testExternalLegalEntity);
    String testExternalSubjectName = "testAnnexOneExternalSubjectName";
    String testExternalSubjectReference = "testAnnexOneExternalSubjectReference";
    final SubjectProfileJaxRs subjectProfileJaxRs =
        createSubjectProfile(
            customerProfile, testExternalSubjectName, testExternalSubjectReference);
    String subjectProfileJaxRsId = subjectProfileJaxRs.id();
    final PIADocumentJaxRs document =
        new PIADocumentJaxRs(
            new PIAAnnex1JaxRs(
                "testAnnex1Question1",
                "testAnnex1Question2",
                "testAnnex1Question3",
                "testAnnex1Question4",
                "testAnnex1Question5",
                "testAnnex1Question6",
                "testAnnex1Question7"),
            new PIAAnnex2JaxRs(
                "testAnnex2Step1",
                "testAnnex2Step2Part1",
                "testAnnex2Step2Part1",
                new PIAAnnex2Step3JaxRs(
                    "testAnnex2Step3Field1",
                    "testAnnex2Step3Field2",
                    "testAnnex2Step3Field3",
                    "testAnnex2Step3Field4"),
                new PIAAnnex2Step4JaxRs(
                    "testAnnex2Step4Field1",
                    "testAnnex2Step4Field2",
                    "testAnnex2Step4Field3",
                    "testAnnex2Step4Field4"),
                new PIAAnnex2Step5JaxRs(
                    "testAnnex2Step5Field1", "testAnnex2Step5Field2", "testAnnex2Step5Field3"),
                new PIAAnnex2Step6JaxRs(
                    "testAnnex2Step6Field1", "testAnnex2Step6Field2", "testAnnex2Step6Field3")),
            new PIAAnnex3JaxRs(
                new PIAAnnex3Principle1JaxRs(
                    "testAnnex3Principle1Question1",
                    "testAnnex3Principle1Question2",
                    "testAnnex3Principle1Question3",
                    "testAnnex3Principle1Question4",
                    "testAnnex3Principle1Question5",
                    "testAnnex3Principle1Question6",
                    "testAnnex3Principle1Question7",
                    "testAnnex3Principle1Question8",
                    "testAnnex3Principle1Question9"),
                new PIAAnnex3Principle2JaxRs(
                    "testAnnex3Principle2Question1", "testAnnex3Principle2Question2"),
                new PIAAnnex3Principle3JaxRs(
                    "testAnnex3Principle3Question1", "testAnnex3Principle3Question2"),
                new PIAAnnex3Principle4JaxRs(
                    "testAnnex3Principle4Question1", "testAnnex3Principle4Question2"),
                new PIAAnnex3Principle5JaxRs(
                    "testAnnex3Principle5Question1", "testAnnex3Principle5Question2"),
                new PIAAnnex3Principle6JaxRs(
                    "testAnnex3Principle6Question1", "testAnnex3Principle6Question2"),
                new PIAAnnex3Principle7JaxRs(
                    "testAnnex3Principle7Question1", "testAnnex3Principle7Question2"),
                new PIAAnnex3Principle8JaxRs(
                    "testAnnex3Principle8Question1", "testAnnex3Principle8Question2")));

    PIAJaxRs piaAnnexOneJaxRs =
        piaInterfaceJaxRs.create(new PIACreateJaxRs(subjectProfileJaxRsId, document));

    assertThat(piaAnnexOneJaxRs.document(), equalTo(document));
    assertThat(piaAnnexOneJaxRs.subjectProfileId(), equalTo(subjectProfileJaxRsId));
  }
}
