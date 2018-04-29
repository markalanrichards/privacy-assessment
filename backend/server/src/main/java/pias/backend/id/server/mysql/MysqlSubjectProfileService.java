package pias.backend.id.server.mysql;

import lombok.extern.log4j.Log4j2;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import pias.backend.id.server.database.SubjectProfileService;
import pias.backend.id.server.entity.SubjectProfile;
import pias.backend.id.server.entity.SubjectProfileCreate;
import pias.backend.id.server.entity.SubjectProfileUpdate;

@Log4j2
public class MysqlSubjectProfileService implements SubjectProfileService{
    public static final Object NULL_OBJECT = null;
    public static final String FIELD_VERSION = "subject_profile_version";
    public static final String FIELD_ID = "subject_profile_id";
    public static final String FIELD_EPOCH = "subject_profile_epoch";
    public static final String TABLE_SUBJECT_PROFILE_IDS = "subject_profile_ids";
    public static final String TABLE_SUBJECT_PROFILES = "subject_profiles";
    private static final String FIELD_SUBJECT_CUSTOMER_PROFILE_ID = "subject_customer_profile_id";
    public static final String LAST_FIELD_VERSION = "LAST_" + FIELD_VERSION;
    public static final String SELECT_VERSION_FOR_ID = String.format(
            "SELECT %s FROM %s WHERE %s = :%s FOR UPDATE",
            FIELD_VERSION,
            TABLE_SUBJECT_PROFILE_IDS,
            FIELD_ID,
            FIELD_ID);
    private static final String FIELD_SUBJECT_REFERENCE = "subject_profile_reference";
    private static final String FIELD_SUBJECT_NAME = "subject_profile_name";
    public static final String SELECT_SUBJECT_PROFILE_BY_ID_AND_VERSION = String.format(
            "SELECT %s, %s, %s, %s FROM %s WHERE %s = :%s AND %s = :%s",
            FIELD_SUBJECT_NAME,
            FIELD_SUBJECT_REFERENCE,
            FIELD_EPOCH,
            FIELD_SUBJECT_CUSTOMER_PROFILE_ID,
            TABLE_SUBJECT_PROFILES,
            FIELD_ID,
            FIELD_ID,
            FIELD_VERSION,
            FIELD_VERSION
    );
    public static final String SELECT_BY_CUSTOMER_PROFILE_ID = "SELECT "
            + TABLE_SUBJECT_PROFILES + "." + FIELD_ID
            + ", " + TABLE_SUBJECT_PROFILES + "." + FIELD_VERSION
            + ", " + TABLE_SUBJECT_PROFILES + "." + FIELD_SUBJECT_NAME
            + ", " + TABLE_SUBJECT_PROFILES + "." + FIELD_SUBJECT_REFERENCE
            + ", " + TABLE_SUBJECT_PROFILES + "." + FIELD_EPOCH
            + " FROM "
            + TABLE_SUBJECT_PROFILES
            + ", " + TABLE_SUBJECT_PROFILE_IDS
            + " WHERE "
            + TABLE_SUBJECT_PROFILES + "." + FIELD_ID
            + " = "
            + TABLE_SUBJECT_PROFILE_IDS + "." + FIELD_ID
            + " AND "
            + TABLE_SUBJECT_PROFILES + "." + FIELD_VERSION
            + " = "
            + TABLE_SUBJECT_PROFILE_IDS + "." + FIELD_VERSION
            + " AND "
            + FIELD_SUBJECT_CUSTOMER_PROFILE_ID + " = :" + FIELD_SUBJECT_CUSTOMER_PROFILE_ID;
    public static final String INSERT_NEW_CUSTOMER_PROFILE = String.format(
            "INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES(:%s, :%s, :%s, :%s, :%s, :%s)",
            TABLE_SUBJECT_PROFILES,
            FIELD_ID,
            FIELD_SUBJECT_NAME,
            FIELD_SUBJECT_REFERENCE,
            FIELD_SUBJECT_CUSTOMER_PROFILE_ID,
            FIELD_VERSION,
            FIELD_EPOCH,
            FIELD_ID,
            FIELD_SUBJECT_NAME,
            FIELD_SUBJECT_REFERENCE,
            FIELD_SUBJECT_CUSTOMER_PROFILE_ID,
            FIELD_VERSION,
            FIELD_EPOCH);
    public static final String INSERT_NEW_CUSTOMER_PROFILE_ID = String.format("INSERT INTO %s (%s, %s) VALUES(:%s, :%s)",
            TABLE_SUBJECT_PROFILE_IDS,
            FIELD_ID,
            FIELD_VERSION,
            FIELD_ID,
            FIELD_VERSION
    );
    private final DBI dbi;

    public MysqlSubjectProfileService(DBI dbi) {
        this.dbi = dbi;

    }

    public static final String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
    public static final ResultSetMapper<Long> MAP_LAST_INSERT_ID = (i, resultSet, statementContext) -> resultSet.getLong(1);

    public SubjectProfile createSubjectProfile(SubjectProfileCreate build) {
        log.debug("Creating {}", build);
        final SubjectProfile created = dbi.inTransaction((handle, transactionStatus) -> {
            final String externalSubjectName = build.getExternalSubjectName();
            final String externalSubjectReference = build.getExternalSubjectReference();
            final Long customerProfileId = build.getCustomerProfileId();
            final Long subjectProfileId = insertNewSubjectProfileId(handle);
            final long epoch = System.currentTimeMillis();
            final SubjectProfile customerProfile = SubjectProfile.builder()
                    .version(0)
                    .id(subjectProfileId)
                    .externalSubjectReference(externalSubjectReference)
                    .externalSubjectName(externalSubjectName)
                    .customerProfileId(customerProfileId)
                    .epoch(epoch)
                    .build();
            insertNewSubjectProfile(handle, customerProfile);
            return customerProfile;

        });
        log.debug("Created {}", created);
        return created;
    }

    private void insertNewSubjectProfile(Handle handle, SubjectProfile subjectProfile) {
        handle.createStatement(INSERT_NEW_CUSTOMER_PROFILE)
                .bind(FIELD_SUBJECT_NAME, subjectProfile.getExternalSubjectName())
                .bind(FIELD_SUBJECT_REFERENCE, subjectProfile.getExternalSubjectReference())
                .bind(FIELD_SUBJECT_CUSTOMER_PROFILE_ID, subjectProfile.getCustomerProfileId())
                .bind(FIELD_ID, subjectProfile.getId())
                .bind(FIELD_EPOCH, subjectProfile.getEpoch())
                .bind(FIELD_VERSION, subjectProfile.getVersion())
                .execute();
    }

    private Long insertNewSubjectProfileId(Handle handle) {
        handle.createStatement(INSERT_NEW_CUSTOMER_PROFILE_ID).bind(FIELD_ID, NULL_OBJECT).bind(FIELD_VERSION, 0).execute();
        return handle.createQuery(SELECT_LAST_INSERT_ID).map(MAP_LAST_INSERT_ID).first();
    }

    public SubjectProfile updateSubjectProfile(SubjectProfileUpdate update) {
        return dbi.inTransaction((handle, transactionStatus) -> {
            final Long id = update.getId();
            final Long lastVersion = update.getLastVersion();
            final long nextVersion = lastVersion + 1L;
            final long epoch = System.currentTimeMillis();
            final boolean updateVersionSuccess = handle.createStatement(String.format("UPDATE %s SET %s = :%s WHERE %s = :%s AND %s = :%s", TABLE_SUBJECT_PROFILE_IDS, FIELD_VERSION, FIELD_VERSION, FIELD_VERSION, LAST_FIELD_VERSION, FIELD_ID, FIELD_ID))
                    .bind(FIELD_VERSION, nextVersion)
                    .bind(FIELD_ID, id)
                    .bind(LAST_FIELD_VERSION, lastVersion)
                    .execute() == 1;
            if (updateVersionSuccess) {
                final SubjectProfile customerProfile = SubjectProfile.builder()
                        .epoch(epoch)
                        .externalSubjectName(update.getExternalSubjectName())
                        .externalSubjectReference(update.getExternalSubjectReference())
                        .customerProfileId(update.getCustomerProfileId())
                        .id(update.getId())
                        .version(nextVersion)
                        .build();
                insertNewSubjectProfile(handle, customerProfile);
                return customerProfile;
            } else {
                throw new IllegalStateException("Cannot update to this version");
            }


        });

    }

    public SubjectProfile readSubjectProfile(long id) {
        log.debug("Reading {}", id);
        return dbi.inTransaction((handle, transactionStatus) -> {
            final long version = selectVersionForId(id, handle);
            return readSubjectProfile(id, handle, version);

        });

    }

    private SubjectProfile readSubjectProfile(long id, Handle handle, long version) {
        return handle.createQuery(SELECT_SUBJECT_PROFILE_BY_ID_AND_VERSION)
                .bind(FIELD_ID, id)
                .bind(FIELD_VERSION, version)
                .map((i, resultSet, statementContext) -> SubjectProfile.builder()
                        .externalSubjectName(resultSet.getString(FIELD_SUBJECT_NAME))
                        .externalSubjectReference(resultSet.getString(FIELD_SUBJECT_REFERENCE))
                        .customerProfileId(resultSet.getLong(FIELD_SUBJECT_CUSTOMER_PROFILE_ID))
                        .id(id)
                        .version(version)
                        .epoch(resultSet.getLong(FIELD_EPOCH))
                        .build()).first();

    }

    private Long selectVersionForId(long id, Handle handle) {
        return handle.createQuery(String.format(SELECT_VERSION_FOR_ID))
                .bind(FIELD_ID, id)
                .map((i, resultSet, statementContext) -> resultSet.getLong(FIELD_VERSION))
                .first();
    }

    public SubjectProfile readSubjectProfile(long id, long version) {
        return dbi.inTransaction((handle, transactionStatus) -> readSubjectProfile(id, handle, version));
    }

    public ImmutableList<SubjectProfile> readSubjectProfilesForCustomerProfile(long customerProfileId) {
        return dbi.inTransaction((handle, transactionStatus) -> Lists.immutable.ofAll(
                handle.createQuery(SELECT_BY_CUSTOMER_PROFILE_ID).bind(FIELD_SUBJECT_CUSTOMER_PROFILE_ID, customerProfileId)
                        .map((i, resultSet, statementContext) -> SubjectProfile.builder()
                                .id(resultSet.getLong(FIELD_ID))
                                .version(resultSet.getLong(FIELD_VERSION))
                                .customerProfileId(customerProfileId)
                                .externalSubjectName(resultSet.getString(FIELD_SUBJECT_NAME))
                                .epoch(resultSet.getLong(FIELD_EPOCH))
                                .externalSubjectReference(resultSet.getString(FIELD_SUBJECT_REFERENCE))
                                .build()).list()));

    }
}
