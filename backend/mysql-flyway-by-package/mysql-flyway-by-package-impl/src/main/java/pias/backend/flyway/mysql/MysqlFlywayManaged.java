package pias.backend.flyway.mysql;

import org.eclipse.collections.api.list.ImmutableList;
import org.flywaydb.core.Flyway;
import pias.backend.flyway.FlywayConfig;
import pias.backend.flyway.FlywayJdbcConfig;
import pias.backend.flyway.FlywayManaged;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;

public class MysqlFlywayManaged implements FlywayManaged {

    private Flyway flyway = new Flyway();
    private final String jdbcUrl;
    private final String user;
    private final String password;
    private final Duration limit;

    public MysqlFlywayManaged(final FlywayConfig flywayConfig) {
        final FlywayJdbcConfig flywayJdbcConfig = flywayConfig.getFlywayJdbcConfig();
        final ImmutableList<String> initSqls = flywayJdbcConfig.getInitSqls();
        jdbcUrl = flywayJdbcConfig.getJdbcUrl();
        user = flywayJdbcConfig.getUser();
        password = flywayJdbcConfig.getPassword();
        limit = Duration.ofSeconds(flywayJdbcConfig.getTimeoutSeconds());
        final String[] initSqlsArray = initSqls.toArray(new String[initSqls.size()]);
        flyway.setDataSource(jdbcUrl, user, password, initSqlsArray);
        flyway.setLocations(flywayConfig.getClassForPackage().getPackage().getName().replace('.', '/'));

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
