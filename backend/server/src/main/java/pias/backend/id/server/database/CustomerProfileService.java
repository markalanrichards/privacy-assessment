package pias.backend.id.server.database;

import pias.backend.id.server.entity.CustomerProfile;
import pias.backend.id.server.entity.CustomerProfileCreate;
import pias.backend.id.server.entity.CustomerProfileUpdate;

public interface CustomerProfileService {
    CustomerProfile create(CustomerProfileCreate customerProfileCreate);

    CustomerProfile update(CustomerProfileUpdate customerProfileUpdate);

    CustomerProfile read(long id);

    CustomerProfile read(long id, long version);
}
