CREATE TABLE customer_profile_ids (
  customer_profile_id BIGINT UNSIGNED NOT NULL UNIQUE AUTO_INCREMENT,
  customer_profile_version BIGINT UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (customer_profile_id, customer_profile_version)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4;


CREATE TABLE customer_profiles (
  customer_profile_id BIGINT UNSIGNED NOT NULL,
  customer_profile_version BIGINT UNSIGNED NOT NULL,
  customer_profile_external_email VARCHAR(190)  NOT NULL,
  customer_profile_external_legal_entity TEXT NOT NULL,
  customer_profile_epoch BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (customer_profile_id, customer_profile_version)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4;;
