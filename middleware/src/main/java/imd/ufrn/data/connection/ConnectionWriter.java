package imd.ufrn.data.connection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import imd.ufrn.data.Writer;

public class ConnectionWriter extends DataOutputStream 
implements Writer {
  private Connection connection;

  public ConnectionWriter(
    OutputStream output, 
    Connection connection
  ) {
    super(output);
    this.connection = connection;
  };

  @Override
  public void send(String content) throws IOException {
    this.write(content.getBytes(StandardCharsets.UTF_8));
    this.flush();
    this.connection.finishOutput();
  };
};
