package pias.backend.flyway.mysqltest;

import java.sql.SQLException;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Query;
import org.skife.jdbi.v2.Update;
import org.skife.jdbi.v2.util.LongColumnMapper;
import pias.backend.flyway.mysql.MysqlFlywayManagedDatabase;

public class MysqlFlywayManagedTest {

  @RegisterExtension
  public MysqlFlywayManagedDatabase flywayManagedDatabase =
      new MysqlFlywayManagedDatabase(MysqlFlywayManagedTest.class);

  @Test
  public void testMigration() throws SQLException {

    final DBI dbi = flywayManagedDatabase.getDbi();

    final Long aLong =
        dbi.inTransaction(
            (handle, transactionStatus) -> {
              final Update statement =
                  handle.createStatement("INSERT INTO contexts(context_id) VALUES(NULL)");
              statement.execute();
              final Query<Map<String, Object>> query =
                  handle.createQuery("SELECT LAST_INSERT_ID()");
              final Query<Long> map = query.map(LongColumnMapper.PRIMITIVE);
              return map.list().get(0);
            });
    Assertions.assertEquals(aLong.longValue(), 1L);
  }
}
