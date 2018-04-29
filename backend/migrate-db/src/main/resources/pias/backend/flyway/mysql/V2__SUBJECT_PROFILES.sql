CREATE TABLE subject_profile_ids (
  subject_profile_id BIGINT UNSIGNED NOT NULL UNIQUE AUTO_INCREMENT,
  subject_profile_version BIGINT UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (subject_profile_id, subject_profile_version)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4;


CREATE TABLE subject_profiles (
  subject_profile_id BIGINT UNSIGNED NOT NULL,
  subject_profile_version BIGINT UNSIGNED NOT NULL,
  subject_customer_profile_id BIGINT UNSIGNED NOT NULL,
  subject_profile_name VARCHAR(190)  NOT NULL,
  subject_profile_reference TEXT NOT NULL,
  subject_profile_epoch BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (subject_profile_id, subject_profile_version)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4;;
