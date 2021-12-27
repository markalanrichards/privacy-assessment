package pias.backend.id.server.entity;

import org.eclipse.collections.api.list.primitive.ImmutableByteList;

public record PIACreate(long subjectProfileId, ImmutableByteList document) {}
