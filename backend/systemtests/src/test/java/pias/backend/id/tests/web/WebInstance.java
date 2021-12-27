package pias.backend.id.tests.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.rules.ExternalResource;
import pias.backend.flyway.FlywayJdbcConfig;
import pias.backend.id.server.PrivacyServer;
import pias.backend.id.server.entity.JdbcConfiguration;
import pias.backend.id.server.entity.PrivacyConfiguration;
import pias.backend.id.test.main.database.DatabaseHandle;
import pias.backend.id.test.main.web.model.WebClient;

public class WebInstance extends ExternalResource {

  private static final Logger LOGGER = LogManager.getLogger(WebInstance.class);
  private DatabaseHandle databaseHandle;

  private PrivacyServer privacyServer;
  private WebClient instance;

  @Override
  public void before() throws Throwable {

    databaseHandle = new DatabaseHandle();

    final int port = 0;
    final String hostname = "127.0.0.1";
    final FlywayJdbcConfig flywayJdbcConfig = databaseHandle.getFlywayJdbcConfig();
    LOGGER.info("Context jdbc {}", flywayJdbcConfig);

    final String database = "mysql";
    final JdbcConfiguration jdbcConfiguration =
        new JdbcConfiguration(
            flywayJdbcConfig.jdbcUrl(), flywayJdbcConfig.user(), flywayJdbcConfig.password());
    final PrivacyConfiguration privacyConfiguration =
        new PrivacyConfiguration(port, hostname, jdbcConfiguration, database);
    privacyServer = new PrivacyServer(privacyConfiguration);
    instance = WebClient.createInstance(privacyServer);
  }

  public WebClient webClient() {
    return instance;
  }

  @Override
  public void after() {
    try {
      databaseHandle.close();
    } finally {
      privacyServer.close();
    }
  }
}
