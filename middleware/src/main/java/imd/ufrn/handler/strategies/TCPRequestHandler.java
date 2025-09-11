package imd.ufrn.handler.strategies;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.Optional;

import imd.ufrn.Marshaller;
import imd.ufrn.data.Reader;
import imd.ufrn.data.Response;
import imd.ufrn.data.connection.Connection;
import imd.ufrn.data.connection.TCPConnection;
import imd.ufrn.data.errors.NotFound;
import imd.ufrn.enums.TransportProtocol;
import imd.ufrn.interceptors.InvocationContext;
import imd.ufrn.invoker.Invoker;
import imd.ufrn.invoker.InvokerEntry;
import imd.ufrn.lookup.Lookup;
import imd.ufrn.lookup.LookupKey;
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
    Reader reader = connection.getReader();

    return () -> {
      try {
        do {
          try {
            InvocationContext context = new InvocationContext();

            LookupKey key = Marshaller
              .getInstance()
              .identify(reader);

            context.setKey(key);
          
            Optional<InvokerEntry> entry = Lookup
              .getInstance()
              .findInvokerEntry(key);

            if(entry.isPresent()) {
              context.setInvoke(entry.get());

              Response<Object> response = Invoker
                .getInstance()
                .invoke(
                  context,
                  reader,
                  entry.get()
                );
              
              connection.getWriter().send(
                response.serialize()
              );
            } else {
              connection.getWriter().send(
                new NotFound().toResponse().serialize()
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
