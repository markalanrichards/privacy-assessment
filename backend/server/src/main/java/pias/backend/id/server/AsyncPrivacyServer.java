package pias.backend.id.server;

import async.AsyncSyncAvroRPCHandler;
import com.beust.jcommander.JCommander;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.zaxxer.hikari.HikariDataSource;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.collections.api.list.primitive.ImmutableByteList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.factory.primitive.ByteLists;
import org.eclipse.jetty.server.Slf4jRequestLogWriter;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.skife.jdbi.v2.DBI;
import pias.backend.PIAAvpr;
import pias.backend.avro.CustomerProfileAvpr;
import pias.backend.avro.SubjectProfileAvpr;
import pias.backend.id.server.avro.CustomerProfileAvprImpl;
import pias.backend.id.server.avro.PIAAvprImpl;
import pias.backend.id.server.avro.SubjectProfileAvprImpl;
import pias.backend.id.server.database.CustomerProfileService;
import pias.backend.id.server.database.PIAService;
import pias.backend.id.server.database.SubjectProfileService;
import pias.backend.id.server.entity.JdbcConfiguration;
import pias.backend.id.server.entity.PrivacyConfiguration;
import pias.backend.id.server.mapping.json.JdbcConfigurationMapper;
import pias.backend.id.server.mapping.json.PrivacyConfigurationMapper;
import pias.backend.id.server.mysql.MysqlCustomerProfileService;
import pias.backend.id.server.mysql.MysqlPIAService;
import pias.backend.id.server.mysql.MysqlSubjectProfileService;
import pias.backend.id.server.postgres.PostgresCustomerProfileService;
import pias.backend.id.server.postgres.PostgresPIAService;
import pias.backend.id.server.postgres.PostgresSubjectProfileService;
import reactor.core.publisher.Mono;

public class AsyncPrivacyServer implements AutoCloseable {
  private final ProjectReactorServerExternalResource server;
  private static final Logger LOGGER = LogManager.getLogger(AsyncPrivacyServer.class);
  private final String hostname;

  public AsyncPrivacyServer(final PrivacyConfiguration privacyConfiguration) {
    Slf4jRequestLogWriter slf4jRequestLogWriter = new Slf4jRequestLogWriter();
    hostname = privacyConfiguration.hostname();
    final ServletHandler handler = new ServletHandler();
    ErrorHandler errorHandler = new ErrorHandler();
    errorHandler.setShowMessageInTitle(false);
    errorHandler.setShowStacks(false);
    final HikariDataSource serverDataSource =
        createHikariDataSource(privacyConfiguration.serverJdbcConfiguration());

    final DBI serverDbi = new DBI(serverDataSource);

    final CustomerProfileService mysqlCustomerProfileService;
    final SubjectProfileService mysqlSubjectProfileService;
    final PIAService mysqlPiaService;
    switch (privacyConfiguration.database()) {
      case "mysql":
        mysqlCustomerProfileService = new MysqlCustomerProfileService(serverDbi);
        mysqlPiaService = new MysqlPIAService(serverDbi);
        mysqlSubjectProfileService = new MysqlSubjectProfileService(serverDbi);
        break;
      case "postgres":
        mysqlCustomerProfileService = new PostgresCustomerProfileService(serverDbi);
        mysqlPiaService = new PostgresPIAService(serverDbi);
        mysqlSubjectProfileService = new PostgresSubjectProfileService(serverDbi);
        break;
      default:
        throw new IllegalStateException();
    }
    final PIAAvprImpl piaAvprImpl = new PIAAvprImpl(mysqlPiaService);

    AsyncSyncAvroRPCHandler piaAsyncSyncAvroRPCHandler =
        new AsyncSyncAvroRPCHandler<PIAAvpr.Callback>(piaAvprImpl, PIAAvpr.Callback.class);
    final String piaPath =
        "/avpr/%s.%s"
            .formatted(
                PIAAvpr.Callback.PROTOCOL.getNamespace(), PIAAvpr.Callback.PROTOCOL.getName());
    CustomerProfileAvpr.Callback customerProfileAvpr =
        new CustomerProfileAvprImpl(mysqlCustomerProfileService);
    AsyncSyncAvroRPCHandler customerAsyncSyncAvroRPCHandler =
        new AsyncSyncAvroRPCHandler<CustomerProfileAvpr.Callback>(
            customerProfileAvpr, CustomerProfileAvpr.Callback.class);
    final String customerPath =
        "/avpr/%s.%s"
            .formatted(
                CustomerProfileAvpr.Callback.PROTOCOL.getNamespace(),
                CustomerProfileAvpr.Callback.PROTOCOL.getName());
    SubjectProfileAvpr.Callback subjectProfileAvpr =
        new SubjectProfileAvprImpl(mysqlSubjectProfileService);
    AsyncSyncAvroRPCHandler subjectAsyncSyncAvroRPCHandler =
        new AsyncSyncAvroRPCHandler<SubjectProfileAvpr.Callback>(
            subjectProfileAvpr, SubjectProfileAvpr.Callback.class);
    final String subjectPath =
        "/avpr/%s.%s"
            .formatted(
                SubjectProfileAvpr.Callback.PROTOCOL.getNamespace(),
                SubjectProfileAvpr.Callback.PROTOCOL.getName());
    ImmutableMap<String, AsyncSyncAvroRPCHandler> avroservices =
        Maps.immutable.of(
            piaPath, piaAsyncSyncAvroRPCHandler,
            subjectPath, subjectAsyncSyncAvroRPCHandler,
            customerPath, customerAsyncSyncAvroRPCHandler);
    server =
        new ProjectReactorServerExternalResource(
            hostname,
            privacyConfiguration.port(),
            routes -> {
              avroservices
                  .keyValuesView()
                  .forEach(
                      pair -> {
                        routes.post(
                            pair.getOne(),
                            (req, res) -> {
                              Mono<ImmutableByteList> bytelist =
                                  req.receive()
                                      .aggregate()
                                      .asByteArray()
                                      .flatMap(
                                          array -> {
                                            try {
                                              CompletableFuture<ImmutableByteList> execute =
                                                  pair.getTwo()
                                                      .execute(ByteLists.immutable.of(array));
                                              return Mono.fromCompletionStage(execute);
                                            } catch (Exception e) {
                                              throw new RuntimeException(e);
                                            }
                                          });

                              return res.send(
                                  bytelist.map(ibl -> Unpooled.copiedBuffer(ibl.toArray())));
                            });
                      });
            });
  }

  private HikariDataSource createHikariDataSource(final JdbcConfiguration jdbcConfiguration) {
    final HikariDataSource ds = new HikariDataSource();
    ds.setJdbcUrl(jdbcConfiguration.jdbcUrl());
    ds.setUsername(jdbcConfiguration.jdbcUsername());
    ds.setPassword(jdbcConfiguration.jdbcPassword());
    return ds;
  }

  public int getPort() {
    return server.getPort();
  }

  public String getHostname() {
    return hostname;
  }

  public static void main(final String args[]) throws IOException, InterruptedException {
    final CommandLineParser commandLineParser = new CommandLineParser();
    JCommander.newBuilder().addObject(commandLineParser).build().parse(args);
    final URL privacyConfigurationUrl = URI.create(commandLineParser.getConfigUrl()).toURL();

    final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
    final JdbcConfigurationMapper jdbcConfigurationMapper =
        new JdbcConfigurationMapper(objectMapper);
    final PrivacyConfigurationMapper privacyConfigurationMapper =
        new PrivacyConfigurationMapper(objectMapper, jdbcConfigurationMapper);
    final ObjectNode objectNode = (ObjectNode) objectMapper.readTree(privacyConfigurationUrl);
    final PrivacyConfiguration privacyConfiguration =
        privacyConfigurationMapper.toPrivacyConfiguration(objectNode);
    final AsyncPrivacyServer privacyServer = new AsyncPrivacyServer(privacyConfiguration);
  }

  public void close() {
    try {
      server.close();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
