package pias.backend.id.server.entity;

public class JdbcConfigurationTest {
    public static final String SAMPLE_JDBC_HOSTNAME = "SAMPLE_JDBC_HOSTNAME";
    public static final String SAMPLE_JDBC_URL = "SAMPLE_JDBC_URL";
    public static final String SAMPLE_JDBC_PASSWORD = "SAMPLE_JDBC_PASSWORD";
    public final static JdbcConfiguration SAMPLE_JDBC_CONFIGURATION = JdbcConfiguration.builder()
            .jdbcUsername(SAMPLE_JDBC_HOSTNAME)
            .jdbcUrl(SAMPLE_JDBC_URL)
            .jdbcPassword(SAMPLE_JDBC_PASSWORD)
            .build();

}