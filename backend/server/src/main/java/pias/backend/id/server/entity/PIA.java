package pias.backend.id.server.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.eclipse.collections.api.list.primitive.ImmutableByteList;

@EqualsAndHashCode
@ToString
@Value
@Builder(toBuilder = true)
public class PIA {
    long version, id, subjectProfileId, epoch;
    ImmutableByteList document;
}
