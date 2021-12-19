package pias.backend.flyway.mysql;

import pias.backend.flyway.FlywayConfig;
import pias.backend.flyway.Migrator;
import pias.backend.flyway.model.MigrationConfig;

import java.util.function.Consumer;
import java.util.function.Function;

public class MysqlMigrator
    implements Function<MigrationConfig, FlywayConfig>, Consumer<MigrationConfig> {
  final Migrator migration =
      new Migrator((migrationConfig) -> new MysqlFlywayManaged(apply(migrationConfig)));

  public FlywayConfig apply(MigrationConfig migrationConfig) {
    return FlywayConfig.builder()
        .classForPackage(MysqlMigrator.class)
        .flywayJdbcConfig(migrationConfig.getServer())
        .build();
  }

  @Override
  public void accept(MigrationConfig migrationConfig) {
    migration.accept(migrationConfig);
  }
}
