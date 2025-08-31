package imd.ufrn.handler.listeners;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.time.Duration;

import imd.ufrn.enums.ApplicationProtocol;
import imd.ufrn.enums.TransportProtocol;
import imd.ufrn.handler.listeners.implementations.ApplicationImplementation;
import lombok.Getter;
import lombok.Setter;

public abstract class ServerListener {
  @Getter
  @Setter
  private ApplicationImplementation application;

  @Getter
  @Setter
  private ServerProcess process;
  
  public abstract TransportProtocol getTransportProtocol();
  public abstract ApplicationProtocol getApplicationProtocol();
  public abstract InetSocketAddress getAddress();
    
  public abstract void bind(
    int port,
    int backlog,
    Duration timeout
  ) throws BindException, IOException;
  
  public abstract Runnable accept() 
    throws IOException;
};
