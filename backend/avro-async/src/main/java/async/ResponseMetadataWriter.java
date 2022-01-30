package async;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.eclipse.collections.api.list.primitive.ImmutableByteList;
import org.eclipse.collections.impl.factory.primitive.ByteLists;

public class ResponseMetadataWriter extends Metadata {
  private static final GenericDatumWriter<Map<String, ByteBuffer>> META_WRITER =
      new GenericDatumWriter<>(META);

  public void write(HashMap<String, ByteBuffer> objectObjectHashMap, BinaryEncoder out)
      throws IOException {
    META_WRITER.write(objectObjectHashMap, out);
  }

  public ImmutableByteList responseMetadata(HashMap<String, ByteBuffer> stringByteBufferHashMap) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    final BinaryEncoder binaryEncoder =
        EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null);
    try {
      META_WRITER.write(stringByteBufferHashMap, binaryEncoder);
      binaryEncoder.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return ByteLists.immutable.of(byteArrayOutputStream.toByteArray());
  }
}
