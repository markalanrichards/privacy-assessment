package pias.backend.id.server.mapping.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pias.backend.id.server.entity.JdbcConfiguration;

public class JdbcConfigurationMapper {

    private static final String
            JDBC_URL = "jdbcUrl",
            JDBC_USERNAME = "jdbcUsername",
            JDBC_PASSWORD = "jdbcPassword";

    private final ObjectMapper objectMapper;

    public JdbcConfigurationMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ObjectNode toJsonObject(final JdbcConfiguration jdbcConfiguration) {

        final ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put(JDBC_URL, jdbcConfiguration.getJdbcUrl());
        objectNode.put(JDBC_USERNAME, jdbcConfiguration.getJdbcUsername());
        objectNode.put(JDBC_PASSWORD, jdbcConfiguration.getJdbcPassword());
        return objectNode;
    }

    public JdbcConfiguration toJdbcConfiguration(final ObjectNode objectNode) {
        return JdbcConfiguration.builder()
                .jdbcPassword(objectNode.get(JDBC_PASSWORD).asText())
                .jdbcUrl(objectNode.get(JDBC_URL).asText())
                .jdbcUsername(objectNode.get(JDBC_USERNAME).asText())
                .build();
    }
}
