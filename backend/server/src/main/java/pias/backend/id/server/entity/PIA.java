package pias.backend.id.server.entity;

import org.eclipse.collections.api.list.primitive.ImmutableByteList;

public record PIA(
    long version, long id, long subjectProfileId, long epoch, ImmutableByteList document) {}
