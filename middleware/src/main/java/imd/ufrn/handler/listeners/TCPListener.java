package imd.ufrn.handler.listeners;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.Optional;

import imd.ufrn.data.Content;
import imd.ufrn.data.connection.Connection;
import imd.ufrn.data.connection.TCPConnection;
import imd.ufrn.enums.ApplicationProtocol;
import imd.ufrn.enums.TransportProtocol;
import lombok.Getter;

@Getter
public class TCPListener extends ServerListener {
  private ServerSocket socket;
  private Duration timeout;

  @Override
  public TransportProtocol getTransportProtocol() {
    return TransportProtocol.TCP;
  };

  @Override
  public ApplicationProtocol getApplicationProtocol() {
    return this.getApplication().getProtocol();
  };

  @Override
  public void bind(
    int port,
    int backlog,
    Duration timeout
  ) throws BindException, IOException {
    this.socket = new ServerSocket(port, backlog);
    this.timeout = timeout;
  };

  @Override
  public InetSocketAddress getAddress() {
    return new InetSocketAddress(
      this.socket.getInetAddress(),
      this.socket.getLocalPort()
    );
  };
  
  @Override
  public Runnable accept() throws IOException {
    Socket client = this.socket.accept();

    int timeout = Long.valueOf(
      this.timeout.toMillis()
    ).intValue();

    client.setSoTimeout(timeout);

    Connection connection = new TCPConnection(client);

    return () -> {
      try {
        do {
          try {
            Content content = this.getApplication().read( 
              connection
            );

            if(content instanceof Error) {
              connection.getWriter().send(
                content.serialize()
              );
            } else {
              Optional<Content> response = this.getProcess().run(
                content,
                Optional.of(connection)
              );

              if(response.isPresent())
                connection.getWriter().send(
                  response.get().serialize()
                );
            };

          } catch (Exception e) {
            if(!connection.isClosed())
              connection.close();
          };
        } while (!connection.isClosed());
      } catch (Exception e) {};
    };
  };
};
