package pias.backend.flyway;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pias.backend.flyway.model.MigrationConfig;

public class Migrator implements Consumer<MigrationConfig> {
  private static final Logger LOGGER = LogManager.getLogger(Migrator.class);
  final Function<MigrationConfig, FlywayManaged> generateFlyway;

  public Migrator(Function<MigrationConfig, FlywayManaged> generateFlyway) {
    this.generateFlyway = generateFlyway;
  }

  @Override
  public void accept(MigrationConfig migrationConfig) {
    LOGGER.error("Starting migration");

    final FlywayManaged flywayManaged = generateFlyway.apply(migrationConfig);
    Stream.of(flywayManaged).forEach(FlywayManaged::migrate);
    LOGGER.error("Stopping migration");
  }
}
