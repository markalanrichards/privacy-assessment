package async;

import java.io.IOException;
import org.apache.avro.Protocol;
import org.apache.avro.io.Decoder;
import org.apache.avro.ipc.HandshakeRequest;
import org.apache.avro.ipc.MD5;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HandshakeReaderStep {
  public static final Logger LOGGER = LogManager.getLogger(HandshakeReaderStep.class);
  private static final SpecificDatumReader<HandshakeRequest> HANDSHAKE_READER =
      new SpecificDatumReader<>(HandshakeRequest.class);

  private final MD5 localHash;
  private final Protocol protocol;

  public HandshakeReaderStep(final Protocol protocol, MD5 hash) {
    this.protocol = protocol;
    localHash = hash;
  }

  public HandshakeRequest read(Decoder in) throws IOException {
    HandshakeRequest handshakeRequest = HANDSHAKE_READER.read(null, in);
    if (localHash.equals(handshakeRequest.getClientHash())) {
      LOGGER.info("Hashes match");
    } else {
      LOGGER.info("Hashes {} {} don't ,match", protocol.getMD5(), handshakeRequest.getClientHash());
    }
    return handshakeRequest;
  }
}
