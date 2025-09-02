package imd.ufrn.handler.listeners;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.time.Duration;

import imd.ufrn.enums.TransportProtocol;

public abstract class RequestHandler {  
  public abstract TransportProtocol getTransportProtocol();
  public abstract InetSocketAddress getAddress();
    
  public abstract void bind(
    int port,
    int backlog,
    Duration timeout
  ) throws BindException, IOException;
  
  public abstract Runnable accept() 
    throws IOException;
};
