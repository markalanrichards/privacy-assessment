package async;

import org.apache.avro.Schema;

public class Metadata {
  protected static final Schema META = Schema.createMap(Schema.create(Schema.Type.BYTES));
}
