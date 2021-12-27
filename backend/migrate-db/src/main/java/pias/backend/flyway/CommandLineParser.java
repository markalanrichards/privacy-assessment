package pias.backend.flyway;

import com.beust.jcommander.Parameter;
import java.util.ArrayList;
import java.util.List;

public class CommandLineParser {
  @Parameter private List<String> parameters = new ArrayList<>();

  @Parameter(
      names = "-migrationConfigUrl",
      description = "Comma-separated list of group names to be run",
      required = true)
  private String migrationConfigUrl;

  public String getMigrationConfigUrl() {
    return migrationConfigUrl;
  }
}
