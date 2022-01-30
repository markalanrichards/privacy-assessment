package async;

import java.io.IOException;
import java.util.Map;
import org.apache.avro.Protocol;
import org.apache.avro.io.Decoder;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.specific.SpecificDatumReader;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.impl.collector.Collectors2;

public class RequestObjectReaderStep {
  private final ImmutableMap<Protocol.Message, SpecificDatumReader<Object>> readers;

  public RequestObjectReaderStep(Protocol protocol, final SpecificData specificData) {

    Map<String, Protocol.Message> messages = protocol.getMessages();
    this.readers =
        messages.entrySet().stream()
            .collect(
                Collectors2.toImmutableMap(
                    e -> e.getValue(),
                    e -> {
                      final Protocol.Message message = e.getValue();
                      return new SpecificDatumReader<Object>(
                          message.getRequest(), message.getRequest(), specificData);
                    }));
  }

  public Object read(Protocol.Message messageName, Decoder in) throws IOException {
    return readers.get(messageName).read(null, in);
  }
}
