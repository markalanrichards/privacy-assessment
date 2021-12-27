package pias.backend.id.server.avro;

import java.util.List;
import org.eclipse.collections.api.list.ImmutableList;
import pias.backend.avro.SubjectProfileAvpr;
import pias.backend.avro.SubjectProfileAvro;
import pias.backend.avro.SubjectProfileCreateAvro;
import pias.backend.avro.SubjectProfileUpdateAvro;
import pias.backend.id.server.database.SubjectProfileService;
import pias.backend.id.server.entity.SubjectProfile;
import pias.backend.id.server.entity.SubjectProfileCreate;
import pias.backend.id.server.entity.SubjectProfileUpdate;

public class SubjectProfileAvprImpl implements SubjectProfileAvpr {
  final SubjectProfileService serviceProfileService;

  public SubjectProfileAvprImpl(SubjectProfileService serviceProfileService) {
    this.serviceProfileService = serviceProfileService;
  }

  @Override
  public SubjectProfileAvro avroCreateSubjectProfile(SubjectProfileCreateAvro request) {

    final Long customerProfileId = Long.valueOf(request.getCustomerProfileId());
    final String externalSubjectName = request.getExternalSubjectName();
    final String externalSubjectReference = request.getExternalSubjectReference();
    final SubjectProfileCreate subjectProfileCreate =
        new SubjectProfileCreate(customerProfileId, externalSubjectName, externalSubjectReference);
    final SubjectProfile subjectProfile =
        serviceProfileService.createSubjectProfile(subjectProfileCreate);
    return toAvro(subjectProfile);
  }

  private SubjectProfileAvro toAvro(SubjectProfile subjectProfile) {

    final String id = String.valueOf(subjectProfile.id());
    final String epoch = String.valueOf(subjectProfile.epoch());
    final String version = String.valueOf(subjectProfile.version());
    final String customerProfileId = String.valueOf(subjectProfile.customerProfileId());
    final String externalSubjectName = subjectProfile.externalSubjectName();
    final String externalSubjectReference = subjectProfile.externalSubjectReference();
    return SubjectProfileAvro.newBuilder()
        .setId(id)
        .setEpoch(epoch)
        .setVersion(version)
        .setCustomerProfileId(customerProfileId)
        .setExternalSubjectName(externalSubjectName)
        .setExternalSubjectReference(externalSubjectReference)
        .build();
  }

  @Override
  public SubjectProfileAvro avroUpdateSubjectProfile(SubjectProfileUpdateAvro update) {

    final Long lastVersion = Long.valueOf(update.getLastVersion());
    final Long id = Long.valueOf(update.getId());
    final Long customerProfileId = Long.valueOf(update.getCustomerProfileId());
    final String externalSubjectName = update.getExternalSubjectName();
    final String externalSubjectReference = update.getExternalSubjectReference();
    final SubjectProfileUpdate subjectProfileUpdate =
        new SubjectProfileUpdate(
            id, lastVersion, customerProfileId, externalSubjectName, externalSubjectReference);
    final SubjectProfile subjectProfile =
        serviceProfileService.updateSubjectProfile(subjectProfileUpdate);
    return toAvro(subjectProfile);
  }

  @Override
  public SubjectProfileAvro avroReadSubjectProfile(String id) {
    SubjectProfile readSubjectProfile = serviceProfileService.readSubjectProfile(Long.valueOf(id));
    return toAvro(readSubjectProfile);
  }

  @Override
  public SubjectProfileAvro avroReadVersionedSubjectProfile(String id, String version) {
    SubjectProfile readSubjectProfile =
        serviceProfileService.readSubjectProfile(Long.valueOf(id), Long.valueOf(version));
    return toAvro(readSubjectProfile);
  }

  @Override
  public List<SubjectProfileAvro> avroReadSubjectProfilesForCustomerProfile(
      String customerProfileId) {
    ImmutableList<SubjectProfile> readSubjectProfile =
        serviceProfileService.readSubjectProfilesForCustomerProfile(
            Long.valueOf(customerProfileId));
    return readSubjectProfile.collect(this::toAvro).toList();
  }
}
