package imd.ufrn.data.connection;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.util.ByteArrayBuilder;

import imd.ufrn.data.Reader;

public class ConnectionReader extends DataInputStream 
implements Reader {
  public ConnectionReader(InputStream input) {
    super(input);
  };

  @Override
  public String readNextLine() throws IOException {
    try (ByteArrayBuilder builder = new ByteArrayBuilder()) {
      byte current = this.readByte();

      while(true) {
        if(current == 13) {
          byte next = this.readByte();
          if(next == 10) break;
          builder.append(current);
          builder.append(next);
        } else {
          builder.append(current);
        };

        current = this.readByte();
      };

      return new String(builder.toByteArray(), StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw e;
    }
  };
};
