package pias.backend.id.test.main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.testcontainers.containers.MySQLContainer;
import pias.backend.flyway.FlywayConfig;
import pias.backend.flyway.FlywayJdbcConfig;
import pias.backend.flyway.mysql.MysqlFlywayManaged;
import pias.backend.flyway.mysql.MysqlMigrator;

public class DatabaseHandle implements AutoCloseable {

  private final MySQLContainer mySQLContainer;

  private final Connection serverConnection;
  private final MysqlFlywayManaged flywayManaged;

  public FlywayJdbcConfig getFlywayJdbcConfig() {
    return flywayJdbcConfig;
  }

  private final FlywayJdbcConfig flywayJdbcConfig;

  public DatabaseHandle() {
    mySQLContainer = new MySQLContainer("mysql:8");
    mySQLContainer.start();
    try {
      final String jdbcUrl = mySQLContainer.getJdbcUrl();
      final String username = mySQLContainer.getUsername();
      final String password = mySQLContainer.getPassword();
      this.serverConnection = DriverManager.getConnection(jdbcUrl, username, password);
      flywayJdbcConfig = new FlywayJdbcConfig(jdbcUrl, username, password, "", 30L);
      final FlywayConfig flywayConfig = new FlywayConfig(flywayJdbcConfig, MysqlMigrator.class);
      flywayManaged = new MysqlFlywayManaged(flywayConfig);
      flywayManaged.migrate();

    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public void close() {
    flywayManaged.clean();
    try {
      serverConnection.close();

    } catch (SQLException e) {
      throw new IllegalStateException(e);
    } finally {
      mySQLContainer.stop();
      mySQLContainer.close();
    }
  }
}
