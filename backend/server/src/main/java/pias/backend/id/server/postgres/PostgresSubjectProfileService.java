package pias.backend.id.server.postgres;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import pias.backend.id.server.database.SubjectProfileService;
import pias.backend.id.server.entity.SubjectProfile;
import pias.backend.id.server.entity.SubjectProfileCreate;
import pias.backend.id.server.entity.SubjectProfileUpdate;

public class PostgresSubjectProfileService implements SubjectProfileService {
  private static final Logger LOGGER = LogManager.getLogger(PostgresSubjectProfileService.class);
  public static final String FIELD_VERSION = "subject_profile_version";
  public static final String FIELD_ID = "subject_profile_id";
  public static final String FIELD_EPOCH = "subject_profile_epoch";
  public static final String TABLE_SUBJECT_PROFILE_IDS = "subject_profile_ids";
  public static final String TABLE_SUBJECT_PROFILES = "subject_profiles";
  private static final String FIELD_SUBJECT_CUSTOMER_PROFILE_ID = "subject_customer_profile_id";
  public static final String LAST_FIELD_VERSION = "LAST_" + FIELD_VERSION;
  public static final String SELECT_VERSION_FOR_ID =
      String.format(
          "SELECT %s FROM %s WHERE %s = :%s FOR UPDATE",
          FIELD_VERSION, TABLE_SUBJECT_PROFILE_IDS, FIELD_ID, FIELD_ID);
  private static final String FIELD_SUBJECT_REFERENCE = "subject_profile_reference";
  private static final String FIELD_SUBJECT_NAME = "subject_profile_name";
  public static final String SELECT_SUBJECT_PROFILE_BY_ID_AND_VERSION =
      String.format(
          "SELECT %s, %s, %s, %s FROM %s WHERE %s = :%s AND %s = :%s",
          FIELD_SUBJECT_NAME,
          FIELD_SUBJECT_REFERENCE,
          FIELD_EPOCH,
          FIELD_SUBJECT_CUSTOMER_PROFILE_ID,
          TABLE_SUBJECT_PROFILES,
          FIELD_ID,
          FIELD_ID,
          FIELD_VERSION,
          FIELD_VERSION);
  public static final String SELECT_BY_CUSTOMER_PROFILE_ID =
      "SELECT "
          + TABLE_SUBJECT_PROFILES
          + "."
          + FIELD_ID
          + ", "
          + TABLE_SUBJECT_PROFILES
          + "."
          + FIELD_VERSION
          + ", "
          + TABLE_SUBJECT_PROFILES
          + "."
          + FIELD_SUBJECT_NAME
          + ", "
          + TABLE_SUBJECT_PROFILES
          + "."
          + FIELD_SUBJECT_REFERENCE
          + ", "
          + TABLE_SUBJECT_PROFILES
          + "."
          + FIELD_EPOCH
          + " FROM "
          + TABLE_SUBJECT_PROFILES
          + ", "
          + TABLE_SUBJECT_PROFILE_IDS
          + " WHERE "
          + TABLE_SUBJECT_PROFILES
          + "."
          + FIELD_ID
          + " = "
          + TABLE_SUBJECT_PROFILE_IDS
          + "."
          + FIELD_ID
          + " AND "
          + TABLE_SUBJECT_PROFILES
          + "."
          + FIELD_VERSION
          + " = "
          + TABLE_SUBJECT_PROFILE_IDS
          + "."
          + FIELD_VERSION
          + " AND "
          + FIELD_SUBJECT_CUSTOMER_PROFILE_ID
          + " = :"
          + FIELD_SUBJECT_CUSTOMER_PROFILE_ID;
  public static final String INSERT_NEW_CUSTOMER_PROFILE =
      String.format(
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
  public static final String INSERT_NEW_CUSTOMER_PROFILE_ID =
      String.format(
          "INSERT INTO %s (%s) VALUES(:%s)",
          TABLE_SUBJECT_PROFILE_IDS, FIELD_VERSION, FIELD_VERSION);

  private final DBI dbi;

  public PostgresSubjectProfileService(DBI dbi) {
    this.dbi = dbi;
  }

  public static final String SELECT_LAST_INSERT_ID =
      String.format(
          "SELECT currval(pg_get_serial_sequence('%s', '%s'));",
          TABLE_SUBJECT_PROFILE_IDS, FIELD_ID);
  public static final ResultSetMapper<Long> MAP_LAST_INSERT_ID =
      (i, resultSet, statementContext) -> resultSet.getLong(1);

  public SubjectProfile createSubjectProfile(SubjectProfileCreate build) {
    LOGGER.debug("Creating {}", build);
    final SubjectProfile created =
        dbi.inTransaction(
            (handle, transactionStatus) -> {
              final String externalSubjectName = build.externalSubjectName();
              final String externalSubjectReference = build.externalSubjectReference();
              final Long customerProfileId = build.customerProfileId();
              final Long subjectProfileId = insertNewSubjectProfileId(handle);
              final long epoch = System.currentTimeMillis();

              final SubjectProfile customerProfile =
                  new SubjectProfile(
                      subjectProfileId,
                      0,
                      epoch,
                      customerProfileId,
                      externalSubjectName,
                      externalSubjectReference);
              insertNewSubjectProfile(handle, customerProfile);
              return customerProfile;
            });
    LOGGER.debug("Created {}", created);
    return created;
  }

  private void insertNewSubjectProfile(Handle handle, SubjectProfile subjectProfile) {
    handle
        .createStatement(INSERT_NEW_CUSTOMER_PROFILE)
        .bind(FIELD_SUBJECT_NAME, subjectProfile.externalSubjectName())
        .bind(FIELD_SUBJECT_REFERENCE, subjectProfile.externalSubjectReference())
        .bind(FIELD_SUBJECT_CUSTOMER_PROFILE_ID, subjectProfile.customerProfileId())
        .bind(FIELD_ID, subjectProfile.id())
        .bind(FIELD_EPOCH, subjectProfile.epoch())
        .bind(FIELD_VERSION, subjectProfile.version())
        .execute();
  }

  private Long insertNewSubjectProfileId(Handle handle) {
    handle.createStatement(INSERT_NEW_CUSTOMER_PROFILE_ID).bind(FIELD_VERSION, 0).execute();
    return handle.createQuery(SELECT_LAST_INSERT_ID).map(MAP_LAST_INSERT_ID).first();
  }

  public SubjectProfile updateSubjectProfile(SubjectProfileUpdate update) {
    return dbi.inTransaction(
        (handle, transactionStatus) -> {
          final Long id = update.id();
          final Long lastVersion = update.lastVersion();
          final long nextVersion = lastVersion + 1L;
          final long epoch = System.currentTimeMillis();
          final boolean updateVersionSuccess =
              handle
                      .createStatement(
                          String.format(
                              "UPDATE %s SET %s = :%s WHERE %s = :%s AND %s = :%s",
                              TABLE_SUBJECT_PROFILE_IDS,
                              FIELD_VERSION,
                              FIELD_VERSION,
                              FIELD_VERSION,
                              LAST_FIELD_VERSION,
                              FIELD_ID,
                              FIELD_ID))
                      .bind(FIELD_VERSION, nextVersion)
                      .bind(FIELD_ID, id)
                      .bind(LAST_FIELD_VERSION, lastVersion)
                      .execute()
                  == 1;
          if (updateVersionSuccess) {

            final Long customerProfileId = update.customerProfileId();
            final String externalSubjectReference = update.externalSubjectReference();
            final String externalSubjectName = update.externalSubjectName();

            final SubjectProfile customerProfile =
                new SubjectProfile(
                    id,
                    nextVersion,
                    epoch,
                    customerProfileId,
                    externalSubjectName,
                    externalSubjectReference);
            insertNewSubjectProfile(handle, customerProfile);
            return customerProfile;
          } else {
            throw new IllegalStateException("Cannot update to this version");
          }
        });
  }

  public SubjectProfile readSubjectProfile(long id) {
    LOGGER.debug("Reading {}", id);
    return dbi.inTransaction(
        (handle, transactionStatus) -> {
          final long version = selectVersionForId(id, handle);
          return readSubjectProfile(id, handle, version);
        });
  }

  private SubjectProfile readSubjectProfile(long id, Handle handle, long version) {
    return handle
        .createQuery(SELECT_SUBJECT_PROFILE_BY_ID_AND_VERSION)
        .bind(FIELD_ID, id)
        .bind(FIELD_VERSION, version)
        .map(
            (i, resultSet, statementContext) -> {
              final String externalSubjectName = resultSet.getString(FIELD_SUBJECT_NAME);
              final String externalSubjectReference = resultSet.getString(FIELD_SUBJECT_REFERENCE);
              final long customerProfileId = resultSet.getLong(FIELD_SUBJECT_CUSTOMER_PROFILE_ID);
              final long epoch = resultSet.getLong(FIELD_EPOCH);
              return new SubjectProfile(
                  id,
                  version,
                  epoch,
                  customerProfileId,
                  externalSubjectName,
                  externalSubjectReference);
            })
        .first();
  }

  private Long selectVersionForId(long id, Handle handle) {
    return handle
        .createQuery(String.format(SELECT_VERSION_FOR_ID))
        .bind(FIELD_ID, id)
        .map((i, resultSet, statementContext) -> resultSet.getLong(FIELD_VERSION))
        .first();
  }

  public SubjectProfile readSubjectProfile(long id, long version) {
    return dbi.inTransaction(
        (handle, transactionStatus) -> readSubjectProfile(id, handle, version));
  }

  public ImmutableList<SubjectProfile> readSubjectProfilesForCustomerProfile(
      long customerProfileId) {
    return dbi.inTransaction(
        (handle, transactionStatus) ->
            Lists.immutable.ofAll(
                handle
                    .createQuery(SELECT_BY_CUSTOMER_PROFILE_ID)
                    .bind(FIELD_SUBJECT_CUSTOMER_PROFILE_ID, customerProfileId)
                    .map(
                        (i, resultSet, statementContext) -> {
                          final long id = resultSet.getLong(FIELD_ID);
                          final long version = resultSet.getLong(FIELD_VERSION);
                          final String externalSubjectName =
                              resultSet.getString(FIELD_SUBJECT_NAME);
                          final long epoch = resultSet.getLong(FIELD_EPOCH);
                          final String externalSubjectReference =
                              resultSet.getString(FIELD_SUBJECT_REFERENCE);
                          return new SubjectProfile(
                              id,
                              version,
                              epoch,
                              customerProfileId,
                              externalSubjectName,
                              externalSubjectReference);
                        })
                    .list()));
  }
}
