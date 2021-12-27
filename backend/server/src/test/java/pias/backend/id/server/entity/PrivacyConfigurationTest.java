package pias.backend.id.server.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pias.backend.id.server.mapping.json.JdbcConfigurationMapper;
import pias.backend.id.server.mapping.json.PrivacyConfigurationMapper;

@ExtendWith(MockitoExtension.class)
public class PrivacyConfigurationTest {
  public static final String SAMPLE_HOSTNAME = "SAMPLE_HOSTNAME";
  public static final String SAMPLE_DATABASE = "SAMPLE_DATABASE";
  public static final int SAMPLE_PORT = 1;

  public static final PrivacyConfiguration SAMPLE_PRIVACY_CONFIGURATION =
      new PrivacyConfiguration(
          SAMPLE_PORT,
          SAMPLE_HOSTNAME,
          JdbcConfigurationTest.SAMPLE_JDBC_CONFIGURATION,
          SAMPLE_DATABASE);

  private PrivacyConfigurationMapper privacyConfigurationMapper;
  @Mock private JdbcConfigurationMapper jdbcConfigurationMapper;

  @Mock private ObjectNode objectNode;

  @BeforeEach
  public void setup() {
    this.privacyConfigurationMapper =
        new PrivacyConfigurationMapper(new ObjectMapper(), jdbcConfigurationMapper);
    Mockito.doReturn(objectNode)
        .when(jdbcConfigurationMapper)
        .toJsonObject(JdbcConfigurationTest.SAMPLE_JDBC_CONFIGURATION);
    Mockito.doReturn(JdbcConfigurationTest.SAMPLE_JDBC_CONFIGURATION)
        .when(jdbcConfigurationMapper)
        .toJdbcConfiguration(objectNode);
  }

  @Test
  public void mapping() {
    final ObjectNode jsonNodes =
        privacyConfigurationMapper.toJsonObject(SAMPLE_PRIVACY_CONFIGURATION);
    final PrivacyConfiguration back = privacyConfigurationMapper.toPrivacyConfiguration(jsonNodes);
    MatcherAssert.assertThat(back, CoreMatchers.equalTo(SAMPLE_PRIVACY_CONFIGURATION));
  }
}
