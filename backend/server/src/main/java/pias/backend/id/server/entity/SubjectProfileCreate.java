package pias.backend.id.server.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@ToString
@Value
@Builder(toBuilder = true)
@EqualsAndHashCode
public class SubjectProfileCreate {
  long customerProfileId;
  String externalSubjectName, externalSubjectReference;
}
