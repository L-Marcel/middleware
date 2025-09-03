package imd.ufrn.data;

import java.io.Closeable;
import java.io.IOException;

public interface Writer extends Closeable {
  public void send(String content) 
    throws IOException;
};
