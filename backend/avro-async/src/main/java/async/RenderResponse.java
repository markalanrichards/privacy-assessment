package async;

import java.util.HashMap;
import org.apache.avro.Protocol;
import org.apache.avro.ipc.MD5;
import org.eclipse.collections.api.list.primitive.ImmutableByteList;
import org.eclipse.collections.api.list.primitive.MutableByteList;
import org.eclipse.collections.impl.factory.primitive.ByteLists;

public class RenderResponse {
  private final HandshakeOutput handshakeWriterStep;
  private final ResponseMetadataWriter requestMetadataWriterStep;

  public RenderResponse(final Protocol protocol, MD5 hash) {
    handshakeWriterStep = new HandshakeOutput(protocol, hash);
    requestMetadataWriterStep = new ResponseMetadataWriter();
  }

  public ImmutableByteList aggregate(
      ImmutableByteList handshakeSection,
      ImmutableByteList responseSection,
      ImmutableByteList responseMetadataSection) {
    final MutableByteList accumulator = ByteLists.mutable.empty();
    writeSection(handshakeSection, accumulator);
    writeSection(responseMetadataSection, accumulator);
    writeSection(responseSection, accumulator);
    writeLength(0, accumulator);
    return accumulator.toImmutable();
  }

  private void writeSection(ImmutableByteList immutableByteList, MutableByteList accumulator) {
    final int length = immutableByteList.size();
    writeLength(length, accumulator);
    accumulator.addAll(immutableByteList);
  }

  private void writeLength(int length, MutableByteList accumulator) {
    accumulator.add((byte) (0xff & (length >>> 24)));
    accumulator.add((byte) (0xff & (length >>> 16)));
    accumulator.add((byte) (0xff & (length >>> 8)));
    accumulator.add((byte) (0xff & length));
  }

  public ImmutableByteList renderResponse(ImmutableByteList responseData) {
    ImmutableByteList handshakeSection = handshakeWriterStep.handshakeOutput();
    ImmutableByteList responseSection = responseData;
    ImmutableByteList responseMetadataSection =
        requestMetadataWriterStep.responseMetadata(new HashMap<>());
    return aggregate(handshakeSection, responseSection, responseMetadataSection);
  }
}
