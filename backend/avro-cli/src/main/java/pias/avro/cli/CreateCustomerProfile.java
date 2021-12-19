package pias.avro.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import pias.backend.avro.CustomerProfileCreateAvro;

@Parameters(commandDescription = "Add a customer")
public class CreateCustomerProfile {

  @Parameter(
      names = {"-legalName", "-l"},
      description = "Customer Legal Name",
      required = true)
  private String legalName;

  @Parameter(
      names = {"-email", "-e"},
      description = "Customer Email",
      required = true)
  private String email;

  public CustomerProfileCreateAvro getCustomerProfileCreateAvro() {

    return CustomerProfileCreateAvro.newBuilder()
        .setExternalLegalName(legalName)
        .setExternalEmail(email)
        .build();
  }
}
