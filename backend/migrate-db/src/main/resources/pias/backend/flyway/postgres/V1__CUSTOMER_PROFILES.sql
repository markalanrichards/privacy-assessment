CREATE TABLE customer_profile_ids (
  customer_profile_id SERIAL,
  customer_profile_version BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (customer_profile_id, customer_profile_version)
) ;


CREATE TABLE customer_profiles (
  customer_profile_id BIGINT NOT NULL,
  customer_profile_version BIGINT NOT NULL,
  customer_profile_external_email VARCHAR(190)  NOT NULL,
  customer_profile_external_legal_entity TEXT NOT NULL,
  customer_profile_epoch BIGINT NOT NULL,
  PRIMARY KEY (customer_profile_id, customer_profile_version)
) ;
