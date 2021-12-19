package pias.backend.flyway.mysql;

import org.junit.rules.ExternalResource;
import org.skife.jdbi.v2.DBI;
import org.testcontainers.containers.MySQLContainer;
import pias.backend.flyway.FlywayConfig;
import pias.backend.flyway.FlywayJdbcConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;

public class MysqlFlywayManagedDatabase extends ExternalResource {
  private MySQLContainer mySQLContainer;
  //    @Override
  //    public Statement apply(Statement var1, Description var2){
  //        mySQLContainer.apply(var1,var2);
  //        return super.apply(var1,var2);
  //    }

  private static final String EMPTY_INIT_SQL = "";
  private final Class<?> classForPackage;
  private Connection connection;
  private MysqlFlywayManaged mysqlFlywayManaged;

  private DBI dbi;

  private String jdbcUrl;

  public MysqlFlywayManagedDatabase(Class<?> classForPackage) {
    this.classForPackage = classForPackage;
  }

  protected void before() throws Throwable {
    mySQLContainer = new MySQLContainer("mysql:8");

    mySQLContainer.withDatabaseName("a" + Math.abs(new Random().nextLong()));
    mySQLContainer.start();

    final String jdbcUrl = mySQLContainer.getJdbcUrl();
    this.jdbcUrl = jdbcUrl;

    connection =
        DriverManager.getConnection(
            this.jdbcUrl, mySQLContainer.getUsername(), mySQLContainer.getPassword());
    dbi = new DBI(this.jdbcUrl, mySQLContainer.getUsername(), mySQLContainer.getPassword());
    mysqlFlywayManaged =
        new MysqlFlywayManaged(
            FlywayConfig.builder()
                .flywayJdbcConfig(
                    FlywayJdbcConfig.builder()
                        .user(mySQLContainer.getUsername())
                        .password(mySQLContainer.getPassword())
                        //                        .initSql(EMPTY_INIT_SQL)
                        .jdbcUrl(this.jdbcUrl)
                        .build())
                .classForPackage(classForPackage)
                .build());
    mysqlFlywayManaged.migrate();
  }

  /** Override to tear down your specific external resource. */
  protected void after() {
    mysqlFlywayManaged.clean();

    try {
      connection.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    mySQLContainer.stop();
    mySQLContainer.close();
  }

  public DBI getDbi() {
    return dbi;
  }
}
