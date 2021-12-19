package pias.avro.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import pias.backend.avro.SubjectProfileCreateAvro;

@Parameters(commandDescription = "Add a subject")
public class CreateSubjectProfile {

  @Parameter(
      names = {"-customer-id", "-c"},
      description = "Customer id",
      required = true)
  private String customerProfileId;

  @Parameter(
      names = {"-name", "-n"},
      description = "Subject Name",
      required = true)
  private String externalSubjectName;

  @Parameter(
      names = {"-reference", "-r"},
      description = "Subject Reference",
      required = true)
  String externalSubjectReference;

  public SubjectProfileCreateAvro getSubjectProfileCreateAvro() {

    return SubjectProfileCreateAvro.newBuilder()
        .setCustomerProfileId(customerProfileId)
        .setExternalSubjectName(externalSubjectName)
        .setExternalSubjectReference(externalSubjectReference)
        .build();
  }
}
