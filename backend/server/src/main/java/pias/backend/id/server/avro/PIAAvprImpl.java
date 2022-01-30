package pias.backend.id.server.avro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.eclipse.collections.api.list.primitive.ImmutableByteList;
import org.eclipse.collections.impl.factory.primitive.ByteLists;
import pias.backend.*;
import pias.backend.id.server.database.PIAService;
import pias.backend.id.server.entity.PIA;
import pias.backend.id.server.entity.PIACreate;
import pias.backend.id.server.entity.PIAUpdate;

public class PIAAvprImpl implements PIAAvpr.Callback {
  private final PIAService mysqlPiaService;
  private DecoderFactory decoderFactory = DecoderFactory.get();
  final SpecificDatumReader<PIADocumentAvro> sdr = new SpecificDatumReader<>(PIADocumentAvro.class);
  final SpecificDatumWriter<PIADocumentAvro> specificDatumWriter =
      new SpecificDatumWriter<>(PIADocumentAvro.class);
  final EncoderFactory encoderFactory = EncoderFactory.get();

  public PIAAvprImpl(PIAService piaService) {
    this.mysqlPiaService = piaService;
  }

  @Override
  public PIAAvro avroCreatePIA(PIACreateAvro request) {
    PIADocumentAvro piaDocumentAvro = request.getDocument();
    ImmutableByteList document = getSerialisedDocument(piaDocumentAvro);
    final Long subjectProfileId = Long.valueOf(request.getSubjectProfileId());
    final PIACreate piaCreate = new PIACreate(subjectProfileId, document);
    final PIA pia = mysqlPiaService.create(piaCreate);
    return convertToPIAAvro(pia);
  }

  public ImmutableByteList getSerialisedDocument(PIADocumentAvro document) {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final BinaryEncoder encoder = encoderFactory.binaryEncoder(out, null);
    try {
      specificDatumWriter.write(document, encoder);
      encoder.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return ByteLists.immutable.of(out.toByteArray());
  }

  private PIAAvro convertToPIAAvro(PIA pia) {
    final ImmutableByteList document = pia.document();
    final PIADocumentAvro read = getPiaDocumentAvro(document);
    return PIAAvro.newBuilder()
        .setId(String.valueOf(pia.id()))
        .setDocument(read)
        .setVersion(String.valueOf(pia.version()))
        .setEpoch(String.valueOf(pia.epoch()))
        .setSubjectProfileId(String.valueOf(pia.subjectProfileId()))
        .build();
  }

  public PIADocumentAvro getPiaDocumentAvro(ImmutableByteList document) {
    final PIADocumentAvro read;
    try {
      read = sdr.read(null, decoderFactory.binaryDecoder(document.toArray(), null));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return read;
  }

  @Override
  public PIAAvro avroUpdatePIA(PIAUpdateAvro update) {
    PIADocumentAvro piaDocumentAvro = update.getDocument();
    ImmutableByteList document = getSerialisedDocument(piaDocumentAvro);

    final Long id = Long.valueOf(update.getId());
    final Long lastVersion = Long.valueOf(update.getLastVersion());
    final Long subjectProfileId = Long.valueOf(update.getSubjectProfileId());
    PIAUpdate piaUpdate = new PIAUpdate(lastVersion, id, subjectProfileId, document);
    final PIA pia = mysqlPiaService.update(piaUpdate);
    return convertToPIAAvro(pia);
  }

  @Override
  public PIAAvro avroReadPIA(String id) {
    final PIA pia = mysqlPiaService.read(Long.valueOf(id));
    return convertToPIAAvro(pia);
  }

  @Override
  public PIAAvro avroReadVersionedPIA(String id, String version) {
    final PIA read = mysqlPiaService.read(Long.valueOf(id), Long.valueOf(version));
    return convertToPIAAvro(read);
  }

  @Override
  public void avroCreatePIA(PIACreateAvro request, org.apache.avro.ipc.Callback<PIAAvro> callback)
      throws IOException {
    CompletableFuture.supplyAsync(() -> avroCreatePIA(request))
        .whenComplete(
            (o, t) -> {
              if (t != null) {
                callback.handleError(t);
              } else {
                callback.handleResult(o);
              }
            });
  }

  @Override
  public void avroUpdatePIA(PIAUpdateAvro update, org.apache.avro.ipc.Callback<PIAAvro> callback)
      throws IOException {
    CompletableFuture.supplyAsync(() -> avroUpdatePIA(update))
        .whenComplete(
            (o, t) -> {
              if (t != null) {
                callback.handleError(t);
              } else {
                callback.handleResult(o);
              }
            });
  }

  @Override
  public void avroReadPIA(String id, org.apache.avro.ipc.Callback<PIAAvro> callback)
      throws IOException {
    CompletableFuture.supplyAsync(() -> avroReadPIA(id))
        .whenComplete(
            (o, t) -> {
              if (t != null) {
                callback.handleError(t);
              } else {
                callback.handleResult(o);
              }
            });
  }

  @Override
  public void avroReadVersionedPIA(
      String id, String version, org.apache.avro.ipc.Callback<PIAAvro> callback)
      throws IOException {
    CompletableFuture.supplyAsync(() -> avroReadVersionedPIA(id, version))
        .whenComplete(
            (o, t) -> {
              if (t != null) {
                callback.handleError(t);
              } else {
                callback.handleResult(o);
              }
            });
  }
}
