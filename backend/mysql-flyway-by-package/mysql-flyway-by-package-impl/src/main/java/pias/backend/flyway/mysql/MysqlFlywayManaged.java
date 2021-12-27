package pias.backend.flyway.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import pias.backend.flyway.FlywayConfig;
import pias.backend.flyway.FlywayJdbcConfig;
import pias.backend.flyway.FlywayManaged;

public class MysqlFlywayManaged implements FlywayManaged {

  private final Flyway flyway;
  private final String jdbcUrl;
  private final String user;
  private final String password;
  private final Duration limit;

  public MysqlFlywayManaged(final FlywayConfig flywayConfig) {
    final FlywayJdbcConfig flywayJdbcConfig = flywayConfig.flywayJdbcConfig();
    jdbcUrl = flywayJdbcConfig.jdbcUrl();
    user = flywayJdbcConfig.user();
    password = flywayJdbcConfig.password();
    limit = Duration.ofSeconds(flywayJdbcConfig.timeoutSeconds());
    final FluentConfiguration fluentConfiguration =
        new FluentConfiguration()
            .dataSource(jdbcUrl, user, password)
            //                .initSql(flywayJdbcConfig.getInitSql())
            .locations(flywayConfig.classForPackage().getPackage().getName().replace('.', '/'));
    flyway = new Flyway(fluentConfiguration);
  }

  @Override
  public void migrate() {
    SQLException lastException = null;
    boolean connected = false;
    long epoch = System.currentTimeMillis();
    long untilEpoch = epoch + limit.toMillis();
    while (!connected && System.currentTimeMillis() < untilEpoch) {

      try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password)) {
        connected = connection.isValid(1);
      } catch (SQLException e) {
        lastException = e;
      }
    }
    if (!connected) {
      if (lastException != null) {
        throw new RuntimeException(lastException);
      } else {
        throw new RuntimeException(String.format("Unable to connect in %d milliseconds", limit));
      }
    }
    flyway.migrate();
  }

  @Override
  public void clean() {
    flyway.clean();
  }
}
