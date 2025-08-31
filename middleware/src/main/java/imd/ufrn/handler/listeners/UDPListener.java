package imd.ufrn.handler.listeners;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;

import imd.ufrn.data.Content;
import imd.ufrn.data.errors.Error;
import imd.ufrn.data.packages.Packet;
import imd.ufrn.enums.ApplicationProtocol;
import imd.ufrn.enums.TransportProtocol;
import lombok.Getter;

@Getter
public class UDPListener extends ServerListener {
  private DatagramSocket socket;

  @Override
  public TransportProtocol getTransportProtocol() {
    return TransportProtocol.UDP;
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
    DatagramPacket datagramPacket = new DatagramPacket(
      new byte[1024], 
      1024,
      packet.address()
    );

    datagramPacket.setData(
      packet.content()
        .getBytes(StandardCharsets.UTF_8)
    );

    socket.send(datagramPacket);
  };
  
  @Override
  public Runnable accept() throws IOException {
    Packet packet = this.receive();

    return () -> {
      try {
        Content content = this.getApplication().read(
          packet.content()
        );

        if(content instanceof Error) {
          this.send(new Packet(
            packet.address(),
            content.serialize()
          ));
        } else {
          Optional<Content> response = this.getProcess().run(
            content,
            Optional.empty()
          );

          if(response.isPresent())
            this.send(new Packet(
              packet.address(),
              response.get().serialize()
            ));
        };
      } catch (Exception e) {};
    };
  };
};
