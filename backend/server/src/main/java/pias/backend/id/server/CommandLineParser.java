package pias.backend.id.server;

import com.beust.jcommander.Parameter;
import java.util.ArrayList;
import java.util.List;

public class CommandLineParser {
  @Parameter private List<String> parameters = new ArrayList<>();

  @Parameter(names = "-configUrl", description = "Url to config", required = true)
  private String configUrl;

  public String getConfigUrl() {
    return configUrl;
  }
}
