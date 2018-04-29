package pias.backend.flyway.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import pias.backend.flyway.FlywayJdbcConfig;

public class FlywayConfigMapper {

    public static final String JDBC_URL = "jdbcUrl";
    public static final String JDBC_USER = "jdbcUsername";
    public static final String JDBC_PASSWORD = "jdbcPassword";

    public FlywayJdbcConfig fromJson(JsonNode jsonNode) {
        String jdbcUrl = jsonNode.get(JDBC_URL).asText();
        String password = jsonNode.get(JDBC_PASSWORD).asText();
        String user = jsonNode.get(JDBC_USER).asText();
        return FlywayJdbcConfig.builder()
                .jdbcUrl(jdbcUrl)
                .password(password)
                .user(user)
                .build();

    }
}
