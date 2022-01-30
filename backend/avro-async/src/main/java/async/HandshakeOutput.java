package async;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.avro.Protocol;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.ipc.HandshakeMatch;
import org.apache.avro.ipc.HandshakeResponse;
import org.apache.avro.ipc.MD5;
import org.apache.avro.specific.SpecificDatumWriter;
import org.eclipse.collections.api.list.primitive.ImmutableByteList;
import org.eclipse.collections.impl.factory.primitive.ByteLists;

public class HandshakeOutput {
  private static final SpecificDatumWriter<HandshakeResponse> HANDSHAKE_WRITER =
      new SpecificDatumWriter<>(HandshakeResponse.class);
  private final ImmutableByteList handshakeOutput;

  public HandshakeOutput(Protocol protocol, MD5 localHash) {
    this.handshakeOutput = buildHandshake(protocol, localHash);
  }

  private ImmutableByteList buildHandshake(Protocol protocol, MD5 localHash) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    final BinaryEncoder binaryEncoder =
        EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null);
    HandshakeResponse response = new HandshakeResponse();
    response.setMatch(HandshakeMatch.BOTH);
    if (response.getMatch() != HandshakeMatch.BOTH) {
      response.setServerProtocol(protocol.toString());
      response.setServerHash(localHash);
    }
    try {
      HANDSHAKE_WRITER.write(response, binaryEncoder);
      binaryEncoder.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return ByteLists.immutable.of(byteArrayOutputStream.toByteArray());
  }

  public ImmutableByteList handshakeOutput() {
    return handshakeOutput;
  }
}
