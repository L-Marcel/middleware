package imd.ufrn.handler.strategies;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import imd.ufrn.Marshaller;
import imd.ufrn.data.connection.Connection;
import imd.ufrn.data.connection.TCPConnection;
import imd.ufrn.enums.TransportProtocol;
import imd.ufrn.reflection.Lookup;
import imd.ufrn.reflection.LookupKey;
import lombok.Getter;

@Getter
public class TCPRequestHandler extends RequestHandler {
  private ServerSocket socket;
  private Duration timeout;

  @Override
  public TransportProtocol getTransportProtocol() {
    return TransportProtocol.TCP;
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
          LookupKey key = Marshaller
            .getInstance()
            .identify(connection);
          
          Lookup
            .getInstance()
            .findRequest(key);
          // try {
          //   Content content = this.getApplication().read( 
          //     connection
          //   );

          //   if(content instanceof Error) {
          //     connection.getWriter().send(
          //       content.serialize()
          //     );
          //   } else {
          //     Optional<Content> response = this.getProcess().run(
          //       content,
          //       Optional.of(connection)
          //     );

          //     if(response.isPresent())
          //       connection.getWriter().send(
          //         response.get().serialize()
          //       );
          //   };

          // } catch (Exception e) {
          //   if(!connection.isClosed())
          //     connection.close();
          // };
        } while (!connection.isClosed());
      } catch (Exception e) {};
    };
  };
};
