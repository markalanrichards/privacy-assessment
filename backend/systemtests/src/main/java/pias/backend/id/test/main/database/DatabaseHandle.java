package pias.backend.id.test.main.database;


import lombok.extern.log4j.Log4j2;
import org.testcontainers.containers.MySQLContainer;
import pias.backend.flyway.FlywayConfig;
import pias.backend.flyway.FlywayJdbcConfig;
import pias.backend.flyway.mysql.MysqlFlywayManaged;
import pias.backend.flyway.mysql.MysqlMigrator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Log4j2
public class DatabaseHandle implements AutoCloseable {

    private final MySQLContainer mySQLContainer;

    private final Connection serverConnection;
    private final MysqlFlywayManaged flywayManaged;

    public FlywayJdbcConfig getFlywayJdbcConfig() {
        return flywayJdbcConfig;
    }

    private final FlywayJdbcConfig flywayJdbcConfig;

    public DatabaseHandle() {
        mySQLContainer = new MySQLContainer("mysql:5.7");
        mySQLContainer.start();
        try {
            final String jdbcUrl = mySQLContainer.getJdbcUrl();
            final String username = mySQLContainer.getUsername();
            final String password = mySQLContainer.getPassword();
            this.serverConnection = DriverManager.getConnection(jdbcUrl, username, password);
            flywayJdbcConfig = FlywayJdbcConfig.builder()
                    .jdbcUrl(jdbcUrl)
                    .user(username)
                    .password(password)
                    .build();
            flywayManaged = new MysqlFlywayManaged(FlywayConfig.builder().classForPackage(MysqlMigrator.class).flywayJdbcConfig(flywayJdbcConfig).build());
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
