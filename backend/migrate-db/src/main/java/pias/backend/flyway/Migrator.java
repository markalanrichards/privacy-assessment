package pias.backend.flyway;

import lombok.extern.slf4j.Slf4j;
import pias.backend.flyway.model.MigrationConfig;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Slf4j
public class Migrator implements Consumer<MigrationConfig> {
  final Function<MigrationConfig, FlywayManaged> generateFlyway;

  public Migrator(Function<MigrationConfig, FlywayManaged> generateFlyway) {
    this.generateFlyway = generateFlyway;
  }

  @Override
  public void accept(MigrationConfig migrationConfig) {
    log.error("Starting migration");

    final FlywayManaged flywayManaged = generateFlyway.apply(migrationConfig);
    Stream.of(flywayManaged).forEach(FlywayManaged::migrate);
    log.error("Stopping migration");
  }
}
