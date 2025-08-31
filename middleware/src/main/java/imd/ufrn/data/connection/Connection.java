package imd.ufrn.data.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.UUID;

import imd.ufrn.enums.TransportProtocol;
import lombok.Getter;

@Getter
public abstract class Connection implements AutoCloseable {
  private UUID uuid = UUID.randomUUID();
  private final TransportProtocol protocol;

  public Connection(TransportProtocol protocol) {
    this.protocol = protocol;
  };
  
  public abstract Socket getSocket();
  public abstract Writer getWriter();
  public abstract Reader getReader();
  public abstract InetSocketAddress getAddress();
  
  public abstract void finishOutput() throws IOException;
  public abstract boolean isClosed();
  public abstract void close() throws IOException;
};
