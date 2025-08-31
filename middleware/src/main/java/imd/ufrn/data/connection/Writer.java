package imd.ufrn.data.connection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Writer extends DataOutputStream {
  public Writer(OutputStream output) {
    super(output);
  };

  public void send(String content) throws IOException {
    this.write(content.getBytes(StandardCharsets.UTF_8));
    this.flush();
  };
};
