package async;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.Decoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.collections.api.list.primitive.ImmutableByteList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.impl.collector.Collectors2;
import org.eclipse.collections.impl.factory.primitive.ByteLists;

public class RequestMetadataReaderStep extends Metadata {
  private static final Logger LOGGER = LogManager.getLogger(RequestMetadataReaderStep.class);
  private static final GenericDatumReader<Map<String, ByteBuffer>> META_READER =
      new GenericDatumReader<>(META);

  public ImmutableMap<String, ImmutableByteList> read(Decoder in) throws IOException {

    Map<String, ByteBuffer> requestMeta = META_READER.read(null, in);
    return requestMeta.entrySet().stream()
        .collect(
            Collectors2.toImmutableMap(
                e -> String.valueOf(e.getKey()),
                e -> {
                  ByteBuffer value = e.getValue();
                  byte[] arr = new byte[value.remaining()];
                  value.get(arr);
                  return ByteLists.immutable.of(arr);
                }));
  }
}
