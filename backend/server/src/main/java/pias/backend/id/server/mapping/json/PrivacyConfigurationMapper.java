package pias.backend.id.server.mapping.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pias.backend.id.server.entity.PrivacyConfiguration;
import pias.backend.id.server.entity.JdbcConfiguration;

public class PrivacyConfigurationMapper {
    private final static String
            PORT = "port",
            HOSTNAME = "hostname",
            SERVER_JDBC_CONFIGURATION = "serverJdbcConfiguration";
    public static final String DATABASE = "database";

    private transient final ObjectMapper objectMapper;
    private transient final JdbcConfigurationMapper jdbcConfigurationMapper;

    public PrivacyConfigurationMapper(ObjectMapper objectMapper, JdbcConfigurationMapper jdbcConfigurationMapper) {
        this.objectMapper = objectMapper;
        this.jdbcConfigurationMapper = jdbcConfigurationMapper;
    }

    public ObjectNode toJsonObject(final PrivacyConfiguration privacyConfiguration) {
        final JdbcConfiguration contextJdbcConfiguration = privacyConfiguration.getServerJdbcConfiguration();
        final String hostname = privacyConfiguration.getHostname();
        final int port = privacyConfiguration.getPort();
        final ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put(PORT, String.valueOf(port));
        objectNode.put(HOSTNAME, String.valueOf(hostname));
        objectNode.set(SERVER_JDBC_CONFIGURATION, jdbcConfigurationMapper.toJsonObject(contextJdbcConfiguration));
        objectNode.put(DATABASE, privacyConfiguration.getDatabase());
        return objectNode;
    }
    public PrivacyConfiguration toPrivacyConfiguration(final ObjectNode objectNode) {
        return PrivacyConfiguration.builder()
                .database(objectNode.get(DATABASE).asText())
                .serverJdbcConfiguration(jdbcConfigurationMapper.toJdbcConfiguration(objectNode.with(SERVER_JDBC_CONFIGURATION)))
                .hostname(objectNode.get(HOSTNAME).asText())
                .port(objectNode.get(PORT).asInt())
                .build();
    }
}
