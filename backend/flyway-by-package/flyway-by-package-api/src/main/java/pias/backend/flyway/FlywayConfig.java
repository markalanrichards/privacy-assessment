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
public class FlywayConfig {
    FlywayJdbcConfig flywayJdbcConfig;
    Class<?> classForPackage;

}
