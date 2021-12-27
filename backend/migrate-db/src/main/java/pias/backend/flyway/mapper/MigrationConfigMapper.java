package pias.backend.flyway.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import pias.backend.flyway.model.MigrationConfig;

public class MigrationConfigMapper {
  public static final String SERVER = "server";
  public static final String DATABASE = "database";
  private final FlywayConfigMapper flywayConfigMapper;

  public MigrationConfigMapper(FlywayConfigMapper flywayConfigMapper) {
    this.flywayConfigMapper = flywayConfigMapper;
  }

  public MigrationConfig fromJson(JsonNode jsonNode) {
    return new MigrationConfig(
        flywayConfigMapper.fromJson(jsonNode.get(SERVER)), jsonNode.get(DATABASE).asText());
  }
}
