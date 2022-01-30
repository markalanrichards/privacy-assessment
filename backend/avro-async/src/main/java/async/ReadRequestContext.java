package async;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.avro.Protocol;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.ipc.HandshakeRequest;
import org.apache.avro.ipc.MD5;
import org.apache.avro.specific.SpecificData;
import org.eclipse.collections.api.list.primitive.ImmutableByteList;
import org.eclipse.collections.api.map.ImmutableMap;

public class ReadRequestContext {
  private final HandshakeReaderStep handshakeReaderStep;
  private final RequestMetadataReaderStep requestMetadataReaderStep;
  private final MessageNameReaderStep messageNameReaderStep;
  private final RequestObjectReaderStep requestObjectReaderStep;

  public ReadRequestContext(final Protocol protocol, MD5 hash, SpecificData specificData) {
    handshakeReaderStep = new HandshakeReaderStep(protocol, hash);
    requestMetadataReaderStep = new RequestMetadataReaderStep();
    messageNameReaderStep = new MessageNameReaderStep(protocol);
    requestObjectReaderStep = new RequestObjectReaderStep(protocol, specificData);
  }

  public RequestContext readRequestcontext(ImmutableByteList immutableByteList) {
    ByteArrayInputStream byteArrayInputStream =
        new ByteArrayInputStream(immutableByteList.toArray());
    final Decoder in = DecoderFactory.get().binaryDecoder(byteArrayInputStream, null);
    try {
      HandshakeRequest handshakeRequest = handshakeReaderStep.read(in);
      ImmutableMap<String, ImmutableByteList> requestMetaData = requestMetadataReaderStep.read(in);
      Protocol.Message message = messageNameReaderStep.read(in);
      Object requestObject = requestObjectReaderStep.read(message, in);
      return new RequestContext(handshakeRequest, requestMetaData, message, requestObject);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
