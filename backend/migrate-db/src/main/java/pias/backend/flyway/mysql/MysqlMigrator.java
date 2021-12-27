package pias.backend.flyway.mysql;

import java.util.function.Consumer;
import java.util.function.Function;
import pias.backend.flyway.FlywayConfig;
import pias.backend.flyway.Migrator;
import pias.backend.flyway.model.MigrationConfig;

public class MysqlMigrator
    implements Function<MigrationConfig, FlywayConfig>, Consumer<MigrationConfig> {
  final Migrator migration =
      new Migrator((migrationConfig) -> new MysqlFlywayManaged(apply(migrationConfig)));

  public FlywayConfig apply(MigrationConfig migrationConfig) {
    final FlywayConfig flywayConfig =
        new FlywayConfig(migrationConfig.server(), MysqlMigrator.class);
    return flywayConfig;
  }

  @Override
  public void accept(MigrationConfig migrationConfig) {
    migration.accept(migrationConfig);
  }
}
