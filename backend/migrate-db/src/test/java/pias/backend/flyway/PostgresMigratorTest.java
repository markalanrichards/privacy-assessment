package pias.backend.flyway;

import java.sql.*;
import java.util.stream.Stream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import pias.backend.flyway.model.MigrationConfig;
import pias.backend.flyway.postgres.PostgresMigrator;

public class PostgresMigratorTest {

  PostgreSQLContainer postgreSQLContainer;

  PostgresMigrator migration;
  private MigrationConfig migrationConfig;

  @Before
  public void setup() {
    postgreSQLContainer = new PostgreSQLContainer("postgres:11");
    postgreSQLContainer.start();
    migration = new PostgresMigrator();
    final MigrationConfig migrationConfig =
        new MigrationConfig(
            getFlyJdbcConfig(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()),
            postgreSQLContainer.getDatabaseName());
    this.migrationConfig = migrationConfig;
  }

  @After
  public void tesrdown() {
    this.postgreSQLContainer.stop();
    this.postgreSQLContainer.close();
  }

  private FlywayJdbcConfig getFlyJdbcConfig(String jdbcUrl, String username, String password) {
    return new FlywayJdbcConfig(jdbcUrl, username, password, "", 30L);
  }

  @Test
  public void testMigrationIds() throws SQLException {
    final String contextLastIds = "customer_profiles";
    String jdbcUrl = migrationConfig.server().jdbcUrl();
    assertTableExistsAndIsEmpty(
        jdbcUrl,
        migrationConfig.server().user(),
        migrationConfig.server().password(),
        contextLastIds);
  }

  private void assertTableExistsAndIsEmpty(
      String jdbcUrl, String username, String password, String... tables) throws SQLException {
    try (final Connection contextConnection =
        DriverManager.getConnection(jdbcUrl, username, password)) {
      migration.accept(migrationConfig);
      Stream.of(tables)
          .forEach(
              table -> {
                try (final PreparedStatement preparedStatement =
                    contextConnection.prepareStatement(String.format("SELECT * FROM %s", table))) {
                  try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    boolean next = resultSet.next();
                    Assert.assertFalse(next);
                  }
                } catch (SQLException e) {
                  throw new RuntimeException(e);
                }
              });
    }
  }
}
