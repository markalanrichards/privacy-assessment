package pias.backend.id.server.mysql;

import lombok.extern.log4j.Log4j2;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.TransactionCallback;
import org.skife.jdbi.v2.TransactionStatus;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import pias.backend.id.server.database.CustomerProfileService;
import pias.backend.id.server.entity.CustomerProfile;
import pias.backend.id.server.entity.CustomerProfileCreate;
import pias.backend.id.server.entity.CustomerProfileUpdate;

@Log4j2
public class MysqlCustomerProfileService implements CustomerProfileService {
  public static final String TABLE_CUSTOMER_PROFILES = "customer_profiles";
  public static final String TABLE_CUSTOMER_PROFILE_IDS = "customer_profile_ids";

  public static final String FIELD_EXTERNAL_EMAIL = "customer_profile_external_email";
  public static final String FIELD_EXTERNAL_LEGAL_ENTITY = "customer_profile_external_legal_entity";
  public static final String FIELD_VERSION = "customer_profile_version";
  public static final String LAST_FIELD_VERSION = "LAST_" + FIELD_VERSION;
  public static final String FIELD_ID = "customer_profile_id";
  public static final String FIELD_EPOCH = "customer_profile_epoch";
  public static final String INSERT_NEW_CUSTOMER_PROFILE =
      String.format(
          "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES(:%s, :%s, :%s, :%s, :%s)",
          TABLE_CUSTOMER_PROFILES,
          FIELD_ID,
          FIELD_EXTERNAL_EMAIL,
          FIELD_EXTERNAL_LEGAL_ENTITY,
          FIELD_VERSION,
          FIELD_EPOCH,
          FIELD_ID,
          FIELD_EXTERNAL_EMAIL,
          FIELD_EXTERNAL_LEGAL_ENTITY,
          FIELD_VERSION,
          FIELD_EPOCH);
  public static final String SELECT_CUSTOMER_PROFILE_BY_ID_AND_VERSION =
      String.format(
          "SELECT %s, %s, %s FROM %s WHERE %s = :%s AND %s = :%s",
          FIELD_EXTERNAL_EMAIL,
          FIELD_EXTERNAL_LEGAL_ENTITY,
          FIELD_EPOCH,
          TABLE_CUSTOMER_PROFILES,
          FIELD_ID,
          FIELD_ID,
          FIELD_VERSION,
          FIELD_VERSION);
  public static final String INSERT_NEW_CUSTOMER_PROFILE_ID =
      String.format(
          "INSERT INTO %s (%s, %s) VALUES(:%s, :%s)",
          TABLE_CUSTOMER_PROFILE_IDS, FIELD_ID, FIELD_VERSION, FIELD_ID, FIELD_VERSION);
  public static final String SELECT_VERSION_FOR_ID =
      String.format(
          "SELECT %s FROM %s WHERE %s = :%s FOR UPDATE",
          FIELD_VERSION, TABLE_CUSTOMER_PROFILE_IDS, FIELD_ID, FIELD_ID);
  public static final String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
  public static final ResultSetMapper<Long> MAP_LAST_INSERT_ID =
      (i, resultSet, statementContext) -> resultSet.getLong(1);
  private final DBI dbi;
  public static final Object NULL_OBJECT = null;

  public MysqlCustomerProfileService(DBI dbi) {
    this.dbi = dbi;
  }

  public CustomerProfile read(long id) {
    log.debug("Reading {}", id);
    return dbi.inTransaction(
        (handle, transactionStatus) -> {
          final long version = selectVersionForId(id, handle);
          return read(id, handle, version);
        });
  }

  private CustomerProfile read(long id, Handle handle, long version) {
    return handle
        .createQuery(SELECT_CUSTOMER_PROFILE_BY_ID_AND_VERSION)
        .bind(FIELD_ID, id)
        .bind(FIELD_VERSION, version)
        .map(
            (i, resultSet, statementContext) ->
                CustomerProfile.builder()
                    .externalEmail(resultSet.getString(FIELD_EXTERNAL_EMAIL))
                    .externalLegalEntity(resultSet.getString(FIELD_EXTERNAL_LEGAL_ENTITY))
                    .id(id)
                    .version(version)
                    .epoch(resultSet.getLong(FIELD_EPOCH))
                    .build())
        .first();
  }

  private Long selectVersionForId(long id, Handle handle) {
    return handle
        .createQuery(String.format(SELECT_VERSION_FOR_ID))
        .bind(FIELD_ID, id)
        .map((i, resultSet, statementContext) -> resultSet.getLong(FIELD_VERSION))
        .first();
  }

  public CustomerProfile create(CustomerProfileCreate customerProfileCreate) {
    log.debug("Creating {}", customerProfileCreate);
    final CustomerProfile created =
        dbi.inTransaction(
            (handle, transactionStatus) -> {
              final String externalEmail = customerProfileCreate.getExternalEmail();
              final String externalLegalEntity = customerProfileCreate.getExternalLegalEntity();
              final Long customerProfileId = insertNewCustomerProfileId(handle);
              final long epoch = System.currentTimeMillis();
              final CustomerProfile customerProfile =
                  CustomerProfile.builder()
                      .version(0)
                      .id(customerProfileId)
                      .externalLegalEntity(externalLegalEntity)
                      .externalEmail(externalEmail)
                      .epoch(epoch)
                      .build();
              insertNewCustomerProfile(handle, customerProfile);
              return customerProfile;
            });
    log.debug("Created {}", created);
    return created;
  }

  private void insertNewCustomerProfile(Handle handle, CustomerProfile customerProfile) {
    handle
        .createStatement(INSERT_NEW_CUSTOMER_PROFILE)
        .bind(FIELD_EXTERNAL_EMAIL, customerProfile.getExternalEmail())
        .bind(FIELD_EXTERNAL_LEGAL_ENTITY, customerProfile.getExternalLegalEntity())
        .bind(FIELD_ID, customerProfile.getId())
        .bind(FIELD_EPOCH, customerProfile.getEpoch())
        .bind(FIELD_VERSION, customerProfile.getVersion())
        .execute();
  }

  private Long insertNewCustomerProfileId(Handle handle) {
    handle
        .createStatement(INSERT_NEW_CUSTOMER_PROFILE_ID)
        .bind(FIELD_ID, NULL_OBJECT)
        .bind(FIELD_VERSION, 0)
        .execute();
    return handle.createQuery(SELECT_LAST_INSERT_ID).map(MAP_LAST_INSERT_ID).first();
  }

  public CustomerProfile update(CustomerProfileUpdate update) {
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
                              TABLE_CUSTOMER_PROFILE_IDS,
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
            final CustomerProfile customerProfile =
                CustomerProfile.builder()
                    .epoch(epoch)
                    .externalEmail(update.getExternalEmail())
                    .externalLegalEntity(update.getExternalLegalEntity())
                    .id(update.getId())
                    .version(nextVersion)
                    .build();
            insertNewCustomerProfile(handle, customerProfile);
            return customerProfile;
          } else {
            throw new IllegalStateException("Cannot update to this version");
          }
        });
  }

  public CustomerProfile read(long id, long version) {
    return dbi.inTransaction(
        new TransactionCallback<CustomerProfile>() {
          @Override
          public CustomerProfile inTransaction(Handle handle, TransactionStatus transactionStatus)
              throws Exception {
            return read(id, handle, version);
          }
        });
  }
}
