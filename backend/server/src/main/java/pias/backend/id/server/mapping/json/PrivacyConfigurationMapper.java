package pias.backend.id.server.mapping.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pias.backend.id.server.entity.JdbcConfiguration;
import pias.backend.id.server.entity.PrivacyConfiguration;

public class PrivacyConfigurationMapper {
  private static final String PORT = "port",
      HOSTNAME = "hostname",
      SERVER_JDBC_CONFIGURATION = "serverJdbcConfiguration";
  public static final String DATABASE = "database";

  private final transient ObjectMapper objectMapper;
  private final transient JdbcConfigurationMapper jdbcConfigurationMapper;

  public PrivacyConfigurationMapper(
      ObjectMapper objectMapper, JdbcConfigurationMapper jdbcConfigurationMapper) {
    this.objectMapper = objectMapper;
    this.jdbcConfigurationMapper = jdbcConfigurationMapper;
  }

  public ObjectNode toJsonObject(final PrivacyConfiguration privacyConfiguration) {
    final JdbcConfiguration contextJdbcConfiguration =
        privacyConfiguration.serverJdbcConfiguration();
    final String hostname = privacyConfiguration.hostname();
    final int port = privacyConfiguration.port();
    final ObjectNode objectNode = objectMapper.createObjectNode();
    objectNode.put(PORT, String.valueOf(port));
    objectNode.put(HOSTNAME, String.valueOf(hostname));
    objectNode.set(
        SERVER_JDBC_CONFIGURATION, jdbcConfigurationMapper.toJsonObject(contextJdbcConfiguration));
    objectNode.put(DATABASE, privacyConfiguration.database());
    return objectNode;
  }

  public PrivacyConfiguration toPrivacyConfiguration(final ObjectNode objectNode) {

    final String database = objectNode.get(DATABASE).asText();
    final JdbcConfiguration serverJdbcConfiguration =
        jdbcConfigurationMapper.toJdbcConfiguration(objectNode.with(SERVER_JDBC_CONFIGURATION));
    final String hostname = objectNode.get(HOSTNAME).asText();
    final int port = objectNode.get(PORT).asInt();
    return new PrivacyConfiguration(port, hostname, serverJdbcConfiguration, database);
  }
}
