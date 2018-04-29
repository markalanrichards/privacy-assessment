package pias.backend.id.server.database;

import org.eclipse.collections.api.list.ImmutableList;
import pias.backend.id.server.entity.SubjectProfile;
import pias.backend.id.server.entity.SubjectProfileCreate;
import pias.backend.id.server.entity.SubjectProfileUpdate;

public interface SubjectProfileService
{
    SubjectProfile createSubjectProfile(SubjectProfileCreate subjectProfileCreate);

    SubjectProfile updateSubjectProfile(SubjectProfileUpdate subjectProfileUpdate);

    SubjectProfile readSubjectProfile(long id);

    ImmutableList<SubjectProfile> readSubjectProfilesForCustomerProfile(long customerProfileId);

    SubjectProfile readSubjectProfile(long id, long version);
}
