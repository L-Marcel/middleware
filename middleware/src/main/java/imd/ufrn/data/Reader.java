package imd.ufrn.data;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;

public interface Reader extends Closeable {
  public String readNextLine() 
    throws IOException, EOFException;
  public byte[] readBytes(int length)
    throws IOException, EOFException;
};
