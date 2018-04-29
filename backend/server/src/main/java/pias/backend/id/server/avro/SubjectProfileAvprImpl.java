package pias.backend.id.server.avro;

import org.apache.avro.AvroRemoteException;
import org.eclipse.collections.api.list.ImmutableList;
import pias.backend.avro.SubjectProfileAvpr;
import pias.backend.avro.SubjectProfileAvro;
import pias.backend.avro.SubjectProfileCreateAvro;
import pias.backend.avro.SubjectProfileUpdateAvro;
import pias.backend.id.server.database.SubjectProfileService;
import pias.backend.id.server.mysql.MysqlSubjectProfileService;
import pias.backend.id.server.entity.SubjectProfile;
import pias.backend.id.server.entity.SubjectProfileCreate;
import pias.backend.id.server.entity.SubjectProfileUpdate;

import java.util.List;

public class SubjectProfileAvprImpl implements SubjectProfileAvpr {
    final SubjectProfileService serviceProfileService;

    public SubjectProfileAvprImpl(SubjectProfileService serviceProfileService) {
        this.serviceProfileService = serviceProfileService;
    }

    @Override
    public SubjectProfileAvro avroCreateSubjectProfile(SubjectProfileCreateAvro request) throws AvroRemoteException {
        final SubjectProfile subjectProfile = serviceProfileService.createSubjectProfile(SubjectProfileCreate.builder()
                .customerProfileId(Long.valueOf(request.getCustomerProfileId()))
                .externalSubjectName(request.getExternalSubjectName().toString())
                .externalSubjectReference(request.getExternalSubjectReference().toString())
                .build());
        return toAvro(subjectProfile);
    }

    private SubjectProfileAvro toAvro(SubjectProfile subjectProfile) {
        return SubjectProfileAvro.newBuilder()
                .setId(String.valueOf(subjectProfile.getId()))
                .setEpoch(String.valueOf(subjectProfile.getEpoch()))
                .setVersion(String.valueOf(subjectProfile.getVersion()))
                .setCustomerProfileId(String.valueOf(subjectProfile.getCustomerProfileId()))
                .setExternalSubjectName(subjectProfile.getExternalSubjectName())
                .setExternalSubjectReference(subjectProfile.getExternalSubjectReference())
                .build();
    }

    @Override
    public SubjectProfileAvro avroUpdateSubjectProfile(SubjectProfileUpdateAvro update) throws AvroRemoteException {
        final SubjectProfile subjectProfile = serviceProfileService.updateSubjectProfile(SubjectProfileUpdate.builder()
                .lastVersion(Long.valueOf(update.getLastVersion()))
                .id(Long.valueOf(update.getId()))
                .customerProfileId(Long.valueOf(update.getCustomerProfileId()))
                .externalSubjectName(update.getExternalSubjectName().toString())
                .externalSubjectReference(update.getExternalSubjectReference().toString())
                .build());
        return toAvro(subjectProfile);
    }

    @Override
    public SubjectProfileAvro avroReadSubjectProfile(String id) throws AvroRemoteException {
        SubjectProfile readSubjectProfile = serviceProfileService.readSubjectProfile(Long.valueOf(id));
        return toAvro(readSubjectProfile);
    }

    @Override
    public SubjectProfileAvro avroReadVersionedSubjectProfile(String id, String  version) throws AvroRemoteException {
        SubjectProfile readSubjectProfile = serviceProfileService.readSubjectProfile(Long.valueOf(id), Long.valueOf(version));
        return toAvro(readSubjectProfile);
    }

    @Override
    public List<SubjectProfileAvro> avroReadSubjectProfilesForCustomerProfile(String customerProfileId) throws AvroRemoteException {
        ImmutableList<SubjectProfile> readSubjectProfile = serviceProfileService.readSubjectProfilesForCustomerProfile(Long.valueOf(customerProfileId));
        return readSubjectProfile.collect(this::toAvro).toList();
    }
}
