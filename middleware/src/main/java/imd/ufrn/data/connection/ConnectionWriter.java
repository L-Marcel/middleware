package imd.ufrn.data.connection;

import java.io.IOException;
import java.io.OutputStream;

public class ConnectionWriter extends Writer {
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
    super.send(content);
    this.connection.finishOutput();
  };
};
