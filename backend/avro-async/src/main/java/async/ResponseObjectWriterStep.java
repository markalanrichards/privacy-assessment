package async;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import org.apache.avro.Protocol;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.specific.SpecificDatumWriter;
import org.eclipse.collections.api.list.primitive.ImmutableByteList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.impl.collector.Collectors2;
import org.eclipse.collections.impl.factory.primitive.ByteLists;

public class ResponseObjectWriterStep {
  private final ImmutableMap<Protocol.Message, SpecificDatumWriter<Object>> writers;

  public ResponseObjectWriterStep(Protocol protocol, final SpecificData specificData) {

    Map<String, Protocol.Message> messages = protocol.getMessages();
    this.writers =
        messages.entrySet().stream()
            .collect(
                Collectors2.toImmutableMap(
                    e -> e.getValue(),
                    e -> {
                      final Protocol.Message message = e.getValue();
                      return new SpecificDatumWriter<>(message.getResponse(), specificData);
                    }));
  }

  public ImmutableByteList write(Protocol.Message message, Object responseObj) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    byteArrayOutputStream.write(0);
    final BinaryEncoder binaryEncoder =
        EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null);
    try {
      writers.get(message).write(responseObj, binaryEncoder);
      binaryEncoder.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return ByteLists.immutable.of(byteArrayOutputStream.toByteArray());
  }
}
