package imd.ufrn;

import java.io.EOFException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import imd.ufrn.data.Reader;
import imd.ufrn.enums.HttpMethod;
import imd.ufrn.lookup.LookupKey;
import imd.ufrn.utils.Serialization;
import lombok.Getter;

public class Marshaller {
  @Getter
  private static final Marshaller instance = new Marshaller();

  public LookupKey identify(
    Reader reader
  ) throws Exception {
    String content = reader.readNextLine();

    if(content.endsWith("HTTP/1.1")) {
      String[] parts = content.split(" ");
      if(parts.length < 2) throw new EOFException();

      String method = parts[0];
      String path = parts[1];

      return new LookupKey(
        HttpMethod.valueOf(method),
        path
      );
    } else throw new EOFException();
  };

  public Object mount(
    Reader reader, 
    Class<Object> type
  ) throws Exception {
    String content = reader.readNextLine();
      
    int contentLength = 0;
    while(!content.trim().isEmpty()) {
      String[] header = content.split(": ");

      StringBuilder builder = new StringBuilder();
      for(int i = 1; i < header.length; i++)
        builder.append(header[i]);

      if("Content-Length".equals(header[0]))
        contentLength = Integer.parseInt(builder.toString());
      
      content = reader.readNextLine();
    };

    try {
      byte[] bytes = new byte[contentLength];
      reader.readFully(bytes);

      content = new String(bytes, StandardCharsets.UTF_8);
      
      return this.deserialize(
        content,
        type
      );
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  };

  public Object deserialize(
    String content,
    Class<Object> type
  ) {
    Optional<? extends Object> object = Serialization.deserialize(
      content,
      type
    );
    
    if(object.isPresent()) 
      return object.get();
    else return null;
  };
};
