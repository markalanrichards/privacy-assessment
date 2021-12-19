package pias.backend.flyway;

import com.beust.jcommander.Parameter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CommandLineParser {
  @Parameter private List<String> parameters = new ArrayList<>();

  @Parameter(
      names = "-migrationConfigUrl",
      description = "Comma-separated list of group names to be run",
      required = true)
  private String migrationConfigUrl;
}
