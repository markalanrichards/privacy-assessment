package pias.backend.flyway.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;
import org.junit.rules.ExternalResource;
import org.skife.jdbi.v2.DBI;
import org.testcontainers.containers.PostgreSQLContainer;
import pias.backend.flyway.FlywayConfig;
import pias.backend.flyway.FlywayJdbcConfig;

public class PostgresFlywayManagedDatabase extends ExternalResource {
  private PostgreSQLContainer postgreSQLContainer;
  //    @Override
  //    public Statement apply(Statement var1, Description var2){
  //        postgreSQLContainer.apply(var1,var2);
  //        return super.apply(var1,var2);
  //    }

  private static final String EMPTY_INIT_SQL = "";
  private final Class<?> classForPackage;
  private Connection connection;
  private PostgresFlywayManaged flywayManaged;

  private DBI dbi;

  private String jdbcUrl;

  public PostgresFlywayManagedDatabase(Class<?> classForPackage) {
    this.classForPackage = classForPackage;
  }

  protected void before() throws Throwable {
    postgreSQLContainer = new PostgreSQLContainer("postgres:11");

    postgreSQLContainer.withDatabaseName("a" + Math.abs(new Random().nextLong()));
    postgreSQLContainer.start();

    jdbcUrl = postgreSQLContainer.getJdbcUrl();
    connection =
        DriverManager.getConnection(
            jdbcUrl, postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword());
    dbi = new DBI(jdbcUrl, postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword());
    final FlywayConfig flywayConfig =
        new FlywayConfig(
            new FlywayJdbcConfig(
                jdbcUrl,
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword(),
                "",
                30L),
            classForPackage);
    flywayManaged = new PostgresFlywayManaged(flywayConfig);
    flywayManaged.migrate();
  }

  /** Override to tear down your specific external resource. */
  protected void after() {
    flywayManaged.clean();

    try {
      connection.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    postgreSQLContainer.stop();
    postgreSQLContainer.close();
  }

  public DBI getDbi() {
    return dbi;
  }
}
