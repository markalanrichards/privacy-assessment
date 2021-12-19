package pias.backend.id.server.mysql;

import org.eclipse.collections.impl.factory.primitive.ByteLists;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import pias.backend.id.server.database.PIAService;
import pias.backend.id.server.entity.PIA;
import pias.backend.id.server.entity.PIACreate;
import pias.backend.id.server.entity.PIAUpdate;

import java.io.IOException;

public class MysqlPIAService implements PIAService {
  private final DBI dbi;
  public static final String TABLE_PIAS = "pias";
  public static final String TABLE_PIA_IDS = "pia_ids";
  public static final String FIELD_ID = "pia_id";
  public static final String FIELD_VERSION = "pia_version";
  public static final String FIELD_EPOCH = "pia_epoch";
  public static final String FIELD_SUBJECT_PROFILE_ID = "pia_subject_profile_id";
  public static final String FIELD_DOCUMENT = "pia_document";
  public static final Object NULL_OBJECT = null;
  public static final String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
  public static final String LAST_FIELD_VERSION = "LAST_" + FIELD_VERSION;
  public static final ResultSetMapper<Long> MAP_LAST_INSERT_ID =
      (i, resultSet, statementContext) -> resultSet.getLong(1);
  public static final String INSERT_NEW_PIA_ANNEX_ONE_ID =
      String.format(
          "INSERT INTO %s (%s, %s) VALUES(:%s, :%s)",
          TABLE_PIA_IDS, FIELD_ID, FIELD_VERSION, FIELD_ID, FIELD_VERSION);
  public static final String SELECT_VERSION_FOR_ID =
      String.format(
          "SELECT %s FROM %s WHERE %s = :%s FOR UPDATE",
          FIELD_VERSION, TABLE_PIA_IDS, FIELD_ID, FIELD_ID);
  public static final String SELECT_BY_ID_VERSION =
      String.format(
          "SELECT %s, %s, %s" + " FROM %s" + " WHERE %s = :%s" + " AND %s = :%s",
          FIELD_DOCUMENT,
          FIELD_SUBJECT_PROFILE_ID,
          FIELD_EPOCH,
          TABLE_PIAS,
          FIELD_VERSION,
          FIELD_VERSION,
          FIELD_ID,
          FIELD_ID);

  public MysqlPIAService(DBI dbi) {
    this.dbi = dbi;
  }

  private void insertNewCustomerProfile(Handle handle, PIA pia) throws IOException {

    handle
        .createStatement(
            "INSERT INTO "
                + TABLE_PIAS
                + "("
                + FIELD_ID
                + ","
                + FIELD_VERSION
                + ","
                + FIELD_EPOCH
                + ","
                + FIELD_SUBJECT_PROFILE_ID
                + ","
                + FIELD_DOCUMENT
                + ") VALUES(:"
                + FIELD_ID
                + ",:"
                + FIELD_VERSION
                + ",:"
                + FIELD_EPOCH
                + ",:"
                + FIELD_SUBJECT_PROFILE_ID
                + ",:"
                + FIELD_DOCUMENT
                + ")")
        .bind(FIELD_ID, pia.getId())
        .bind(FIELD_VERSION, pia.getVersion())
        .bind(FIELD_EPOCH, pia.getEpoch())
        .bind(FIELD_SUBJECT_PROFILE_ID, pia.getSubjectProfileId())
        .bind(FIELD_DOCUMENT, pia.getDocument().toArray())
        .execute();
  }

  private Long insertNewPrivacyImpactAssessmentAnnexOneId(Handle handle) {

    handle
        .createStatement(INSERT_NEW_PIA_ANNEX_ONE_ID)
        .bind(FIELD_ID, NULL_OBJECT)
        .bind(FIELD_VERSION, 0L)
        .execute();
    return handle.createQuery(SELECT_LAST_INSERT_ID).map(MAP_LAST_INSERT_ID).first();
  }

  public PIA create(PIACreate piaDocument) {

    return dbi.inTransaction(
        (handle, transactionStatus) -> {
          final Long privacyImpactAssessmentAnnexOneID =
              insertNewPrivacyImpactAssessmentAnnexOneId(handle);

          final PIA pia =
              PIA.builder()
                  .version(0)
                  .id(privacyImpactAssessmentAnnexOneID)
                  .epoch(System.currentTimeMillis())
                  .document(piaDocument.getDocument())
                  .subjectProfileId(Long.valueOf(piaDocument.getSubjectProfileId()))
                  .build();
          insertNewCustomerProfile(handle, pia);
          return pia;
        });
  }

  public PIA read(long id) {
    return dbi.inTransaction(
        (handle, transactionStatus) -> {
          final long version = selectVersionForId(id, handle);
          return read(id, handle, version);
        });
  }

  private Long selectVersionForId(long id, Handle handle) {
    final Long first =
        handle
            .createQuery(String.format(SELECT_VERSION_FOR_ID))
            .bind(FIELD_ID, id)
            .map((i, resultSet, statementContext) -> resultSet.getLong(FIELD_VERSION))
            .first();

    return first;
  }

  private PIA read(long id, Handle handle, long version) {
    return handle
        .createQuery(SELECT_BY_ID_VERSION)
        .bind(FIELD_ID, id)
        .bind(FIELD_VERSION, version)
        .map(
            (i, resultSet, statementContext) -> {
              byte[] bytes = resultSet.getBytes(FIELD_DOCUMENT);
              return PIA.builder()
                  .id(id)
                  .version(version)
                  .epoch(resultSet.getLong(FIELD_EPOCH))
                  .subjectProfileId(resultSet.getLong(FIELD_SUBJECT_PROFILE_ID))
                  .document(ByteLists.immutable.of(bytes))
                  .build();
            })
        .first();
  }

  public PIA read(long id, long version) {
    return dbi.inTransaction((handle, transactionStatus) -> read(id, handle, version));
  }

  public PIA update(PIAUpdate update) {

    return dbi.inTransaction(
        (handle, transactionStatus) -> {
          final Long id = update.getId();
          final Long lastVersion = update.getLastVersion();
          final long nextVersion = lastVersion + 1L;
          final long epoch = System.currentTimeMillis();
          final boolean updateVersionSuccess =
              handle
                      .createStatement(
                          String.format(
                              "UPDATE %s SET %s = :%s WHERE %s = :%s AND %s = :%s",
                              TABLE_PIA_IDS,
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
            final PIA customerProfile =
                PIA.builder()
                    .epoch(epoch)
                    .id(update.getId())
                    .version(nextVersion)
                    .subjectProfileId(update.getSubjectProfileId())
                    .document(update.getDocument())
                    .build();
            insertNewCustomerProfile(handle, customerProfile);
            return customerProfile;
          } else {
            throw new IllegalStateException("Cannot update to this version");
          }
        });
  }
}
