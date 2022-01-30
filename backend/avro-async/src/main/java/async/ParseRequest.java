package async;

import java.io.ByteArrayOutputStream;
import java.util.NoSuchElementException;
import org.eclipse.collections.api.iterator.ByteIterator;
import org.eclipse.collections.api.list.primitive.ImmutableByteList;
import org.eclipse.collections.impl.factory.primitive.ByteLists;

public class ParseRequest {

  public ImmutableByteList readToImmutableByteList(ImmutableByteList inList) {
    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    final ByteIterator byteIterator = inList.byteIterator();
    while (true) {
      byte first = byteIterator.next();
      byte second = byteIterator.next();
      byte third = byteIterator.next();
      byte fourth = byteIterator.next();
      int length =
          ((0xFF & first) << 24)
              + ((0xFF & second) << 16)
              + ((0xFF & third) << 8)
              + (0xFF & fourth);
      if (length == 0) { // end of buffers
        return ByteLists.immutable.of(byteArrayOutputStream.toByteArray());
      } else {
        for (int i = 0; i < length; i++) {
          try {
            byteArrayOutputStream.write(byteIterator.next());
          } catch (NoSuchElementException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }
  }
}
