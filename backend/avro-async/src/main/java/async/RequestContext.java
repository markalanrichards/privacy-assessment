package async;

import org.apache.avro.Protocol;
import org.apache.avro.ipc.HandshakeRequest;
import org.eclipse.collections.api.list.primitive.ImmutableByteList;
import org.eclipse.collections.api.map.ImmutableMap;

public record RequestContext(
    HandshakeRequest handshakeRequest,
    ImmutableMap<String, ImmutableByteList> requestMetaData,
    Protocol.Message message,
    Object requestObject) {}
