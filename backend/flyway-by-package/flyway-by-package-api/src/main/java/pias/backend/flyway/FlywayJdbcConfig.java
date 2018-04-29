package pias.backend.flyway;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

@Value
@EqualsAndHashCode
@Builder(toBuilder = true)
@ToString
public class FlywayJdbcConfig {
    String jdbcUrl;
    String user;
    String password;
    ImmutableList<String> initSqls;
    long timeoutSeconds;

    public static class FlywayJdbcConfigBuilder {
        ImmutableList<String> initSqls = Lists.immutable.empty();
        long timeoutSeconds = 30L;
    }
}
