package pias.backend.id.server.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@ToString
@Value
@Builder(toBuilder = true)
@EqualsAndHashCode
public class SubjectProfile {
  long id, version, epoch, customerProfileId;
  String externalSubjectName, externalSubjectReference;
}
