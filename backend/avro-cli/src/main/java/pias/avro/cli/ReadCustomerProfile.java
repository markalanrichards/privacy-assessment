package pias.avro.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import java.util.Optional;

@Parameters(commandDescription = "get a customer")
public class ReadCustomerProfile {

  public String getId() {
    return id;
  }

  public Optional<String> getVersion() {
    return Optional.ofNullable(version);
  }

  @Parameter(
      names = {"-id", "-i"},
      description = "Customer id",
      required = true)
  private String id;

  @Parameter(
      names = {"-version", "-v"},
      description = "Customer Version",
      required = false)
  private String version;
}
