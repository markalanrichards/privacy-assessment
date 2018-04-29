CREATE TABLE subject_profile_ids (
  subject_profile_id SERIAL,
  subject_profile_version BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (subject_profile_id, subject_profile_version)
) ;


CREATE TABLE subject_profiles (
  subject_profile_id BIGINT NOT NULL,
  subject_profile_version BIGINT NOT NULL,
  subject_customer_profile_id BIGINT NOT NULL,
  subject_profile_name VARCHAR(190)  NOT NULL,
  subject_profile_reference TEXT NOT NULL,
  subject_profile_epoch BIGINT NOT NULL,
  PRIMARY KEY (subject_profile_id, subject_profile_version)
) ;
