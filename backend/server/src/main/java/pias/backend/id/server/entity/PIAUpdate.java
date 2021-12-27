package pias.backend.id.server.entity;

import org.eclipse.collections.api.list.primitive.ImmutableByteList;

public record PIAUpdate(
    long lastVersion, long id, long subjectProfileId, ImmutableByteList document) {}
