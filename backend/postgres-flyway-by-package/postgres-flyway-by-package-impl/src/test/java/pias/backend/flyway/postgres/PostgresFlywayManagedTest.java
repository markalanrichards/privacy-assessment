package pias.backend.flyway.postgres;

import java.sql.SQLException;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Query;
import org.skife.jdbi.v2.Update;
import org.skife.jdbi.v2.util.LongColumnMapper;

public class PostgresFlywayManagedTest {

  @RegisterExtension
  public PostgresFlywayManagedDatabase flywayManagedDatabase =
      new PostgresFlywayManagedDatabase(PostgresFlywayManagedTest.class);

  @Test
  public void testMigration() throws SQLException {

    final DBI dbi = flywayManagedDatabase.getDbi();

    final Long aLong =
        dbi.inTransaction(
            (handle, transactionStatus) -> {
              final Update statement =
                  handle.createStatement("INSERT INTO contexts(context_id) VALUES(DEFAULT)");
              statement.execute();
              final Query<Map<String, Object>> query =
                  handle.createQuery(
                      "SELECT currval(pg_get_serial_sequence('contexts', 'context_id'));");
              final Query<Long> map = query.map(LongColumnMapper.PRIMITIVE);
              return map.list().get(0);
            });
    Assertions.assertEquals(aLong.longValue(), 1L);
  }
}
