package async;

import java.io.IOException;
import org.apache.avro.Protocol;
import org.apache.avro.io.Decoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.impl.factory.Maps;

public class MessageNameReaderStep {
  private final ImmutableMap<String, Protocol.Message> messages;

  private static final Logger LOGGER = LogManager.getLogger(MessageNameReaderStep.class);

  public MessageNameReaderStep(final Protocol protocol) {
    messages = Maps.immutable.ofAll(protocol.getMessages());
  }

  public String readName(Decoder in) throws IOException {
    String messageName = in.readString(null).toString();
    LOGGER.info("Message Name: " + messageName);
    return messageName;
  }

  public Protocol.Message read(Decoder in) throws IOException {
    String messageName = in.readString(null).toString();
    LOGGER.info("Message Name: " + messageName);
    Protocol.Message message = messages.get(messageName);
    return message;
  }
}
