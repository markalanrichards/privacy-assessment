package pias.backend.id.server.avro;

import org.apache.avro.AvroRemoteException;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.eclipse.collections.api.list.primitive.ImmutableByteList;
import org.eclipse.collections.impl.factory.primitive.ByteLists;
import pias.backend.PIAAvpr;
import pias.backend.PIAAvro;
import pias.backend.PIACreateAvro;
import pias.backend.PIADocumentAvro;
import pias.backend.PIAUpdateAvro;
import pias.backend.id.server.database.PIAService;
import pias.backend.id.server.mysql.MysqlPIAService;
import pias.backend.id.server.entity.PIA;
import pias.backend.id.server.entity.PIACreate;
import pias.backend.id.server.entity.PIAUpdate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PIAAvprImpl implements PIAAvpr {
    private final PIAService mysqlPiaService;
    private DecoderFactory decoderFactory = DecoderFactory.get();
    final SpecificDatumReader<PIADocumentAvro> sdr = new SpecificDatumReader<>(PIADocumentAvro.class);
    final SpecificDatumWriter<PIADocumentAvro> specificDatumWriter = new SpecificDatumWriter<>(PIADocumentAvro.class);
    final EncoderFactory encoderFactory = EncoderFactory.get();

    public PIAAvprImpl(PIAService piaService) {
        this.mysqlPiaService = piaService;
    }

    @Override
    public PIAAvro avroCreatePIA(PIACreateAvro request) throws AvroRemoteException {
        PIADocumentAvro document = request.getDocument();
        ImmutableByteList of = getSerialisedDocument(document);
        String subjectProfileId = request.getSubjectProfileId();
        final PIA pia = mysqlPiaService.create(PIACreate.builder()
                .subjectProfileId(Long.valueOf(subjectProfileId))
                .document(of)
                .build());
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
        final ImmutableByteList document = pia.getDocument();
        final PIADocumentAvro read = getPiaDocumentAvro(document);
        return PIAAvro.newBuilder()
                .setId(String.valueOf(pia.getId()))
                .setDocument(read)
                .setVersion(String.valueOf(pia.getVersion()))
                .setEpoch(String.valueOf(pia.getEpoch()))
                .setSubjectProfileId(String.valueOf(pia.getSubjectProfileId()))
                .build();
    }

    public PIADocumentAvro getPiaDocumentAvro(ImmutableByteList document) {
        final PIADocumentAvro read;
        try {
            read = sdr.read(null, decoderFactory.binaryDecoder(document.toArray(), null));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return read;
    }

    @Override
    public PIAAvro avroUpdatePIA(PIAUpdateAvro update) throws AvroRemoteException {
        PIADocumentAvro document = update.getDocument();
        ImmutableByteList of = getSerialisedDocument(document);
        PIAUpdate piaUpdate = PIAUpdate.builder()
                .document(of)
                .id(Long.valueOf(update.getId()))
                .lastVersion(Long.valueOf(update.getLastVersion()))
                .subjectProfileId(Long.valueOf(update.getSubjectProfileId()))
                .build();
        final PIA pia = mysqlPiaService.update(piaUpdate);
        return convertToPIAAvro(pia);
    }

    @Override
    public PIAAvro avroReadPIA(String id) throws AvroRemoteException {
        final PIA pia = mysqlPiaService.read(Long.valueOf(id));
        return convertToPIAAvro(pia);
    }

    @Override
    public PIAAvro avroReadVersionedPIA(String id, String version) throws AvroRemoteException {
        final PIA read = mysqlPiaService.read(Long.valueOf(id), Long.valueOf(version));
        return convertToPIAAvro(read);
    }
}
