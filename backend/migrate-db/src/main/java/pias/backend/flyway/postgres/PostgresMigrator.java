package pias.backend.flyway.postgres;

import pias.backend.flyway.FlywayConfig;
import pias.backend.flyway.Migrator;
import pias.backend.flyway.model.MigrationConfig;

import java.util.function.Consumer;

public class PostgresMigrator implements Consumer<MigrationConfig> {
    final Migrator migration = new Migrator((migrationConfig) -> new PostgresFlywayManaged(FlywayConfig.builder()
            .classForPackage(PostgresMigrator.class)
            .flywayJdbcConfig(migrationConfig.getServer())
            .build()));


    @Override
    public void accept(MigrationConfig migrationConfig) {
        migration.accept(migrationConfig);
    }
}
