CREATE TABLE pia_ids (
  pia_id SERIAL,
  pia_version BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (pia_id, pia_version)
);


CREATE TABLE pias (
  pia_id BIGINT NOT NULL,
  pia_version BIGINT NOT NULL,
  pia_subject_profile_id BIGINT NOT NULL,
  pia_epoch BIGINT NOT NULL,
  pia_document BYTEA NOT NULL,
  PRIMARY KEY (pia_id, pia_version)
) ;
