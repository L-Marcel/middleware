package imd.ufrn.data.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import imd.ufrn.data.Reader;
import imd.ufrn.data.Writer;
import imd.ufrn.enums.TransportProtocol;
import lombok.Getter;

@Getter
public class TCPConnection extends Connection {
  private Writer writer;
  private Reader reader;
  private Socket socket;

  public TCPConnection(Socket socket) throws IOException {
    super(TransportProtocol.TCP);
    this.socket = socket;

    this.writer = new ConnectionWriter(
      socket.getOutputStream(),
      this
    );

    this.reader = new ConnectionReader(
      socket.getInputStream()
    );
  };

  @Override
  public boolean isClosed() {
    return this.socket.isClosed();
  };

  @Override
  public void close() throws IOException {
    this.writer.close();
    this.reader.close();
    this.socket.close();
  };

  @Override
  public void finishOutput() throws IOException {
    if(!this.socket.isOutputShutdown() && !this.getSocket().getKeepAlive())
      this.socket.shutdownOutput();
  }

  @Override
  public InetSocketAddress getAddress() {
    return new InetSocketAddress(
      this.socket.getInetAddress(),
      this.socket.getLocalPort()
    );
  };
};
