package pias.backend.id.server.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.eclipse.collections.api.list.primitive.ImmutableByteList;

@Value
@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
public class PIACreate {
  long subjectProfileId;
  ImmutableByteList document;
}
