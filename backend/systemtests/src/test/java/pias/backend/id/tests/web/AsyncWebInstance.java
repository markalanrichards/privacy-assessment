package pias.backend.id.tests.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import pias.backend.flyway.FlywayJdbcConfig;
import pias.backend.id.server.AsyncPrivacyServer;
import pias.backend.id.server.entity.JdbcConfiguration;
import pias.backend.id.server.entity.PrivacyConfiguration;
import pias.backend.id.test.main.database.DatabaseHandle;

public class AsyncWebInstance implements BeforeEachCallback, AfterEachCallback {

  private static final Logger LOGGER = LogManager.getLogger(AsyncWebInstance.class);
  private DatabaseHandle databaseHandle;

  private AsyncPrivacyServer privacyServer;

  @Override
  public void beforeEach(ExtensionContext context) {

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
    privacyServer = new AsyncPrivacyServer(privacyConfiguration);
  }

  public String urlPrefix() {
    return "http://%s:%d/".formatted(privacyServer.getHostname(), privacyServer.getPort());
  }

  @Override
  public void afterEach(ExtensionContext context) {
    try {
      databaseHandle.close();
    } finally {
      privacyServer.close();
    }
  }
}
