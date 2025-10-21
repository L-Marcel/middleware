package imd.ufrn.handler.strategies;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.time.Duration;

import imd.ufrn.enums.TransportProtocol;

public interface RequestHandler {  
  public TransportProtocol getTransportProtocol();
  public InetSocketAddress getAddress();
    
  public void bind(
    int port,
    int backlog,
    Duration timeout
  ) throws BindException, IOException;
  
  public Runnable accept() 
    throws IOException;
};
