package imd.ufrn.handler.strategies;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;

import imd.ufrn.Marshaller;
import imd.ufrn.data.Response;
import imd.ufrn.data.errors.NotFound;
import imd.ufrn.data.packages.Packet;
import imd.ufrn.data.packages.PacketReader;
import imd.ufrn.enums.TransportProtocol;
import imd.ufrn.interceptors.InvocationContext;
import imd.ufrn.invoker.Invoker;
import imd.ufrn.invoker.InvokerEntry;
import imd.ufrn.lookup.Lookup;
import imd.ufrn.lookup.LookupKey;
import lombok.Getter;

@Getter
public class UDPRequestHandler extends RequestHandler {
  private DatagramSocket socket;

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
    this.socket = new DatagramSocket(port);
  };

  @Override
  public InetSocketAddress getAddress() {
    return new InetSocketAddress(
      this.socket.getInetAddress(),
      this.socket.getLocalPort()
    );
  };

  private Packet receive() throws IOException {
    DatagramPacket packet = new DatagramPacket(
      new byte[1024], 
      1024
    );
    
    this.socket.receive(packet);

    String content = new String(
      packet.getData(),
      StandardCharsets.UTF_8
    );

    return new Packet(
      new InetSocketAddress(
        packet.getAddress(),
        packet.getPort()
      ),
      content
    );
  };

  private void send(Packet packet) throws IOException {
    DatagramPacket datagram = new DatagramPacket(
      new byte[1024], 
      1024,
      packet.address()
    );

    datagram.setData(
      packet.content()
        .getBytes(StandardCharsets.UTF_8)
    );

    socket.send(datagram);
  };
  
  @Override
  public Runnable accept() throws IOException {
    Packet packet = this.receive();
    PacketReader reader = new PacketReader(packet);

    return () -> {
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

          Response<? extends Object> response = Invoker
            .getInstance()
            .invoke(
              context,
              reader,
              entry.get()
            );
          
          this.send(new Packet(
            packet.address(),
            response.serialize()
          ));
        } else {
          this.send(new Packet(
            packet.address(),
            new NotFound().toResponse().serialize()
          ));
        };
      } catch (Exception e) {};
    };
  };
};
