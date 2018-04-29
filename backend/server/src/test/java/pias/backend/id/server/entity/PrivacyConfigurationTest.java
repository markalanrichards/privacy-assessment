package pias.backend.id.server.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pias.backend.id.server.mapping.json.PrivacyConfigurationMapper;
import pias.backend.id.server.mapping.json.JdbcConfigurationMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PrivacyConfigurationTest {
    public static final String SAMPLE_HOSTNAME = "SAMPLE_HOSTNAME";
    public static final String SAMPLE_DATABASE = "SAMPLE_DATABASE";
    public static final int SAMPLE_PORT = 1;
    public static final PrivacyConfiguration SAMPLE_PRIVACY_CONFIGURATION = PrivacyConfiguration.builder()
            .port(SAMPLE_PORT)
            .serverJdbcConfiguration(JdbcConfigurationTest.SAMPLE_JDBC_CONFIGURATION)
            .hostname(SAMPLE_HOSTNAME)
            .database(SAMPLE_DATABASE)
            .build();

    private PrivacyConfigurationMapper privacyConfigurationMapper;
    @Mock
    private JdbcConfigurationMapper jdbcConfigurationMapper;

    @Mock
    private ObjectNode objectNode;

    @Before
    public void setup() {
        this.privacyConfigurationMapper = new PrivacyConfigurationMapper(new ObjectMapper(), jdbcConfigurationMapper);
        Mockito.doReturn(objectNode).when(jdbcConfigurationMapper).toJsonObject(JdbcConfigurationTest.SAMPLE_JDBC_CONFIGURATION);
        Mockito.doReturn(JdbcConfigurationTest.SAMPLE_JDBC_CONFIGURATION).when(jdbcConfigurationMapper).toJdbcConfiguration(objectNode);
    }

    @Test
    public void mapping() {
        final ObjectNode jsonNodes = privacyConfigurationMapper.toJsonObject(SAMPLE_PRIVACY_CONFIGURATION);
        final PrivacyConfiguration back = privacyConfigurationMapper.toPrivacyConfiguration(jsonNodes);
        Assert.assertThat(back, CoreMatchers.equalTo(SAMPLE_PRIVACY_CONFIGURATION));
    }

}