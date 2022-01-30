package pias.backend;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.avro.AvroRuntimeException;
import org.apache.avro.Protocol;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.ipc.HandshakeMatch;
import org.apache.avro.ipc.HandshakeRequest;
import org.apache.avro.ipc.HandshakeResponse;
import org.apache.avro.ipc.MD5;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.util.ByteBufferInputStream;
import org.apache.avro.util.ByteBufferOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SyncAvroRPCHandler<T> {
  private static final Logger LOGGER = LogManager.getLogger(SyncAvroRPCHandler.class);
  private static final Schema META = Schema.createMap(Schema.create(Schema.Type.BYTES));
  private static final GenericDatumReader<Map<String, ByteBuffer>> META_READER =
      new GenericDatumReader<>(META);
  private static final GenericDatumWriter<Map<String, ByteBuffer>> META_WRITER =
      new GenericDatumWriter<>(META);

  private SpecificDatumReader<HandshakeRequest> handshakeReader =
      new SpecificDatumReader<>(HandshakeRequest.class);
  private SpecificDatumWriter<HandshakeResponse> handshakeWriter =
      new SpecificDatumWriter<>(HandshakeResponse.class);
  private final T service;
  private final Class<T> clazz;

  public SyncAvroRPCHandler(T service, Class<T> clazz) {
    this.service = service;
    this.clazz = clazz;
  }

  public List<ByteBuffer> read(List<ByteBuffer> buffers) throws Exception {
    Protocol protocol = new SpecificData(clazz.getClassLoader()).getProtocol(clazz);
    Decoder in = DecoderFactory.get().binaryDecoder(new ByteBufferInputStream(buffers), null);
    HandshakeRequest request = handshakeReader.read(null, in);
    MD5 localHash = new MD5();
    localHash.bytes(protocol.getMD5());

    if (localHash.equals(request.getClientHash())) {
      LOGGER.info("Hashes match");
    } else {
      LOGGER.info("Hashes {} {} dpm#t ,match", protocol.getMD5(), request.getClientHash());
    }

    Map<String, ByteBuffer> requestMeta = META_READER.read(null, in);
    LOGGER.info("Request meta: " + requestMeta.size());
    String messageName = in.readString(null).toString();
    LOGGER.info("Message Name: " + messageName);
    Map<String, Protocol.Message> messages = protocol.getMessages();
    Protocol.Message message = messages.get(messageName);
    GenericDatumReader<Object> objectGenericDatumReader =
        new GenericDatumReader<>(message.getRequest(), message.getRequest(), GenericData.get());

    Object read = objectGenericDatumReader.read(null, in);
    LOGGER.info("Object read: " + read);
    final Object responseObj = respond(message, read);
    LOGGER.info("Object response: " + responseObj);
    LOGGER.info("Object respones class:" + responseObj.getClass());
    ByteBufferOutputStream bbo = new ByteBufferOutputStream();
    BinaryEncoder out = EncoderFactory.get().binaryEncoder(bbo, null);
    HandshakeResponse response = new HandshakeResponse();
    response.setMatch(HandshakeMatch.BOTH);
    if (response.getMatch() != HandshakeMatch.BOTH) {
      response.setServerProtocol(protocol.toString());
      response.setServerHash(localHash);
    }
    handshakeWriter.write(response, out);
    out.flush();
    List<ByteBuffer> handshake = bbo.getBufferList();

    out.writeBoolean(false);
    GenericDatumWriter<Object> objectGenericDatumWriter =
        new GenericDatumWriter<>(message.getResponse(), GenericData.get());
    objectGenericDatumWriter.write(responseObj, out);
    out.flush();
    List<ByteBuffer> payload = null;
    payload = bbo.getBufferList();

    META_WRITER.write(new HashMap<>(), out);
    out.flush();
    // Prepend handshake and append payload
    bbo.prepend(handshake);
    bbo.append(payload);

    return bbo.getBufferList();

    //        String messageName = in.readString(null).toString();
  }

  public Object respond(Protocol.Message message, Object request) throws Exception {
    int numParams = message.getRequest().getFields().size();
    Object[] params = new Object[numParams];
    Class[] paramTypes = new Class[numParams];
    int i = 0;
    SpecificData specificData = new SpecificData(service.getClass().getClassLoader());
    try {
      for (Schema.Field param : message.getRequest().getFields()) {
        params[i] = ((GenericRecord) request).get(param.name());
        paramTypes[i] = specificData.getClass(param.schema());
        i++;
      }
      Method method = service.getClass().getMethod(message.getName(), paramTypes);
      method.setAccessible(true);
      return method.invoke(service, params);
    } catch (InvocationTargetException e) {
      Throwable error = e.getTargetException();
      if (error instanceof Exception) {
        throw (Exception) error;
      } else {
        throw new AvroRuntimeException(error);
      }
    } catch (NoSuchMethodException | IllegalAccessException e) {
      throw new AvroRuntimeException(e);
    }
  }
}
