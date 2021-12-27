package pias.backend.flyway.postgres;

import java.util.function.Consumer;
import pias.backend.flyway.FlywayConfig;
import pias.backend.flyway.Migrator;
import pias.backend.flyway.model.MigrationConfig;

public class PostgresMigrator implements Consumer<MigrationConfig> {
  final Migrator migration =
      new Migrator(
          (migrationConfig) -> {
            final FlywayConfig flywayConfig =
                new FlywayConfig(migrationConfig.server(), PostgresMigrator.class);
            return new PostgresFlywayManaged(flywayConfig);
          });

  @Override
  public void accept(MigrationConfig migrationConfig) {
    migration.accept(migrationConfig);
  }
}
