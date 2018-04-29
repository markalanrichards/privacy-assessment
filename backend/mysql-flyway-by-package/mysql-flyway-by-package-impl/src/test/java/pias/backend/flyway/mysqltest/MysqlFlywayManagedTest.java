package pias.backend.flyway.mysqltest;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.skife.jdbi.v2.*;
import org.skife.jdbi.v2.util.LongColumnMapper;
import pias.backend.flyway.mysql.MysqlFlywayManagedDatabase;

import java.sql.*;
import java.util.Map;

public class MysqlFlywayManagedTest {

    @Rule
    public MysqlFlywayManagedDatabase flywayManagedDatabase = new MysqlFlywayManagedDatabase(MysqlFlywayManagedTest.class);

    @Test
    public void testMigration() throws SQLException {

        final DBI dbi = flywayManagedDatabase.getDbi();

        final Long aLong = dbi.inTransaction((handle, transactionStatus) -> {
            final Update statement = handle.createStatement("INSERT INTO contexts(context_id) VALUES(NULL)");
            statement.execute();
            final Query<Map<String, Object>> query = handle.createQuery("SELECT LAST_INSERT_ID()");
            final Query<Long> map = query.map(LongColumnMapper.PRIMITIVE);
            return map.list().get(0);


        });
        Assert.assertEquals(aLong.longValue(), 1L);


    }

}