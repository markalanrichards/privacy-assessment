package pias.backend.id.server;


import com.beust.jcommander.JCommander;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.collections.api.block.procedure.Procedure2;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.tuple.Tuples;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.Slf4jRequestLog;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.skife.jdbi.v2.DBI;
import pias.backend.id.server.avro.PIAAvprImpl;
import pias.backend.id.server.database.CustomerProfileService;
import pias.backend.id.server.database.PIAService;
import pias.backend.id.server.database.SubjectProfileService;
import pias.backend.id.server.mysql.MysqlCustomerProfileService;
import pias.backend.id.server.mysql.MysqlPIAService;
import pias.backend.id.server.mysql.MysqlSubjectProfileService;
import pias.backend.id.server.entity.PrivacyConfiguration;
import pias.backend.id.server.entity.JdbcConfiguration;
import pias.backend.id.server.jaxrs.PIASApplication;
import pias.backend.id.server.mapping.json.PrivacyConfigurationMapper;
import pias.backend.id.server.mapping.json.JdbcConfigurationMapper;
import pias.backend.id.server.postgres.PostgresCustomerProfileService;
import pias.backend.id.server.postgres.PostgresPIAService;
import pias.backend.id.server.postgres.PostgresSubjectProfileService;
import pias.backend.id.server.servlets.AvroServlets;
import pias.backend.id.server.servlets.PingServlet;

import javax.servlet.Servlet;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class PrivacyServer implements AutoCloseable {
    private final Server server;
    private static final Logger LOGGER = LogManager.getLogger(PrivacyServer.class);
    private final String hostname;

    public PrivacyServer(final PrivacyConfiguration privacyConfiguration) {
        Slf4jRequestLog slf4jRequestLog = new Slf4jRequestLog();
        hostname = privacyConfiguration.getHostname();
        server = new Server();
        final ServletHandler handler = new ServletHandler();
        final ServerConnector serverConnector = new ServerConnector(server);
        serverConnector.setHost(hostname);
        serverConnector.setPort(privacyConfiguration.getPort());
        server.setConnectors(new Connector[]{
                serverConnector
        });
        server.setHandler(handler);
        server.setRequestLog(slf4jRequestLog);
        ErrorHandler errorHandler = new ErrorHandler();
        errorHandler.setShowMessageInTitle(false);
        errorHandler.setShowStacks(false);
        server.setErrorHandler(errorHandler);

        final HikariDataSource serverDataSource = createHikariDataSource(privacyConfiguration.getServerJdbcConfiguration());

        final DBI serverDbi = new DBI(serverDataSource);

        final CustomerProfileService mysqlCustomerProfileService;
        final SubjectProfileService mysqlSubjectProfileService;
        final PIAService mysqlPiaService ;
        switch (privacyConfiguration.getDatabase()) {
            case"mysql":
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
        final PIASApplication app = new PIASApplication(mysqlCustomerProfileService, mysqlSubjectProfileService, mysqlPiaService, piaAvprImpl);
        final CXFNonSpringJaxrsServlet cxfNonSpringJaxrsServlet = new CXFNonSpringJaxrsServlet(app);


        final ImmutableMap<String, Servlet> servlets1 = new AvroServlets().avroServlets(
                mysqlCustomerProfileService,
                mysqlSubjectProfileService,
                piaAvprImpl).collect((k, v) -> Tuples.pair("/avpr/" + k, v));
        final MutableMap<String, Servlet> servlets = Maps.mutable.<String, Servlet>empty()
                .withKeyValue("/ping", new PingServlet())
                .withKeyValue("/rest/*", cxfNonSpringJaxrsServlet)
                .withAllKeyValues(servlets1.keyValuesView());


        final ImmutableMap<String, Servlet> pathMappingToServlet = servlets.toImmutable();


        pathMappingToServlet.forEachKeyValue((Procedure2<String, Servlet>) (s, servlet) -> handler.addServletWithMapping(new ServletHolder(servlet), s));

        try {
            start();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to start server");
        }
    }

    private HikariDataSource createHikariDataSource(final JdbcConfiguration jdbcConfiguration) {
        final HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(jdbcConfiguration.getJdbcUrl());
        ds.setUsername(jdbcConfiguration.getJdbcUsername());
        ds.setPassword(jdbcConfiguration.getJdbcPassword());
        return ds;
    }

    public int getPort() {
        final ServerConnector connector = (ServerConnector) server.getConnectors()[0];

        final int port = connector.getLocalPort();
        LOGGER.debug("localPort {}", port);

        return port;
    }

    public String getHostname() {
        return hostname;
    }


    private void start() throws Exception {
        server.start();

    }

    public void join() throws InterruptedException {
        server.join();
    }

    public static void main(final String args[]) throws IOException, InterruptedException {
        final CommandLineParser commandLineParser = new CommandLineParser();
        JCommander.newBuilder()
                .addObject(commandLineParser)
                .build()
                .parse(args);
        final URL privacyConfigurationUrl = URI.create(commandLineParser.getConfigUrl()).toURL();

        final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        final JdbcConfigurationMapper jdbcConfigurationMapper = new JdbcConfigurationMapper(objectMapper);
        final PrivacyConfigurationMapper privacyConfigurationMapper = new PrivacyConfigurationMapper(objectMapper, jdbcConfigurationMapper);
        final ObjectNode objectNode = (ObjectNode) objectMapper.readTree(privacyConfigurationUrl);
        final PrivacyConfiguration privacyConfiguration = privacyConfigurationMapper.toPrivacyConfiguration(objectNode);
        final PrivacyServer privacyServer = new PrivacyServer(privacyConfiguration);
        privacyServer.join();
    }

    public void close() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
