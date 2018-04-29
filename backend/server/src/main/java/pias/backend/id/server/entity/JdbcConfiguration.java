package pias.backend.id.server.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode
@ToString
@Builder
public class JdbcConfiguration {
    String jdbcUrl;
    String jdbcUsername;
    String jdbcPassword;
}
