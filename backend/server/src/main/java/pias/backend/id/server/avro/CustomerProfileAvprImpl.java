package pias.backend.id.server.avro;

import org.apache.avro.AvroRemoteException;
import pias.backend.avro.CustomerProfileAvpr;
import pias.backend.avro.CustomerProfileAvro;
import pias.backend.avro.CustomerProfileCreateAvro;
import pias.backend.avro.CustomerProfileUpdateAvro;
import pias.backend.id.server.database.CustomerProfileService;
import pias.backend.id.server.mysql.MysqlCustomerProfileService;
import pias.backend.id.server.entity.CustomerProfile;
import pias.backend.id.server.entity.CustomerProfileCreate;
import pias.backend.id.server.entity.CustomerProfileUpdate;

public class CustomerProfileAvprImpl implements CustomerProfileAvpr {
    private final CustomerProfileService mysqlCustomerProfileService;

    public CustomerProfileAvprImpl(CustomerProfileService mysqlCustomerProfileService) {
        this.mysqlCustomerProfileService = mysqlCustomerProfileService;
    }

    @Override
    public CustomerProfileAvro avroCreateCustomerProfile(CustomerProfileCreateAvro request) throws AvroRemoteException {

        return convertToAvro(mysqlCustomerProfileService.create(CustomerProfileCreate.builder()
                .externalEmail(request.getExternalEmail().toString())
                .externalLegalEntity(request.getExternalLegalName().toString())
                .build()));
    }

    @Override
    public CustomerProfileAvro avroUpdateCustomerProfile(CustomerProfileUpdateAvro update) throws AvroRemoteException {
        final CustomerProfile updatedCustomerProfile = mysqlCustomerProfileService.update(CustomerProfileUpdate.builder()
                .externalEmail(update.getExternalEmail().toString())
                .externalLegalEntity(update.getExternalLegalName().toString())
                .id(Long.valueOf(update.getId()))
                .lastVersion(Long.valueOf(update.getLastVersion()))

                .build());
        return convertToAvro(updatedCustomerProfile);
    }

    @Override
    public CustomerProfileAvro avroReadCustomerProfile(String id) throws AvroRemoteException {
        final CustomerProfile read = mysqlCustomerProfileService.read(Long.valueOf(id));
        return convertToAvro(read);
    }

    private CustomerProfileAvro convertToAvro(CustomerProfile read) {
        return CustomerProfileAvro.newBuilder()
                .setExternalLegalName(read.getExternalLegalEntity())
                .setExternalEmail(read.getExternalEmail())
                .setId(String.valueOf(read.getId()))
                .setVersion(String.valueOf(read.getVersion()))
                .setEpoch(String.valueOf(read.getEpoch()))
                .build();
    }

    @Override
    public CustomerProfileAvro avroReadVersionedCustomerProfile(String id, String version) throws AvroRemoteException {
        final CustomerProfile read = mysqlCustomerProfileService.read(Long.valueOf(id), Long.valueOf(version));
        return convertToAvro(read);
    }
}
