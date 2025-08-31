package imd.ufrn.handler.listeners.implementations;

import imd.ufrn.data.Content;
import imd.ufrn.data.connection.Connection;
import imd.ufrn.enums.ApplicationProtocol;

public interface ApplicationImplementation {
  public abstract ApplicationProtocol getProtocol();
  
  public Content read(
    Connection connection
  ) throws Exception;
  
  public Content read(
    String content
  ) throws Exception;
};
