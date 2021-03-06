package pias.backend.id.server.mapping.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pias.backend.id.server.entity.JdbcConfiguration;
import pias.backend.id.server.entity.JdbcConfigurationTest;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JdbcConfigurationMapperTest {


    private JdbcConfigurationMapper jdbcConfigurationMapper;

    @Before
    public void setup() {
        this.jdbcConfigurationMapper = new JdbcConfigurationMapper(new ObjectMapper());
    }

    @Test
    public void mapping() {
        final ObjectNode jsonNodes = jdbcConfigurationMapper.toJsonObject(JdbcConfigurationTest.SAMPLE_JDBC_CONFIGURATION);
        final JdbcConfiguration back = jdbcConfigurationMapper.toJdbcConfiguration(jsonNodes);
        Assert.assertThat(back, CoreMatchers.equalTo(JdbcConfigurationTest.SAMPLE_JDBC_CONFIGURATION));
    }


}