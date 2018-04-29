package pias.backend.flyway;

import com.beust.jcommander.JCommander;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import pias.backend.flyway.mapper.FlywayConfigMapper;
import pias.backend.flyway.mapper.MigrationConfigMapper;
import pias.backend.flyway.model.MigrationConfig;
import pias.backend.flyway.mysql.MysqlMigrator;
import pias.backend.flyway.postgres.PostgresMigrator;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

@Slf4j
public class Migration {


    public static void main(String args[]) throws IOException {
        final CommandLineParser commandLineParser = new CommandLineParser();
        JCommander.newBuilder()
                .addObject(commandLineParser)
                .build()
                .parse(args);
        final URL migrationConfigUrl = URI.create(commandLineParser.getMigrationConfigUrl()).toURL();

        final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

        final ObjectNode objectNode = (ObjectNode) objectMapper.readTree(migrationConfigUrl);
        final MigrationConfigMapper migrationConfigMapper = new MigrationConfigMapper(new FlywayConfigMapper());
        final MigrationConfig migrationConfig = migrationConfigMapper.fromJson(objectNode);
        switch (migrationConfig.getDatabase()) {
            case "mysql":
                new MysqlMigrator().accept(migrationConfig);
                break;
            case "postgres":
                new PostgresMigrator().accept(migrationConfig);
                break;
            default:
                throw new IllegalArgumentException("Unknown database");
        }


    }

}
