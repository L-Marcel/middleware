package imd.ufrn;

import java.io.EOFException;

import imd.ufrn.data.connection.Connection;
import imd.ufrn.data.connection.Reader;
import imd.ufrn.enums.HttpMethod;
import imd.ufrn.reflection.LookupKey;
import lombok.Getter;

public class Marshaller {
  @Getter
  private static final Marshaller instance = new Marshaller();

  public LookupKey identify(String content) throws Exception {
    // TODO - Implementar versão do UDP
    throw new EOFException();
  };

  public LookupKey identify(Connection connection) throws Exception {
    Reader reader = connection.getReader();
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
      
      // Headers headers = new Headers();
      // Optional<Request<Object>> request = Lookup
      //   .getInstance()
      //   .findRequest(
      //     HttpMethod.valueOf(method),
      //     path
      //   );

      // content = reader.readNextLine();
      // while(!content.trim().isEmpty()) {
      //   String[] header = content.split(": ");

      //   StringBuilder builder = new StringBuilder();
      //   for(int i = 1; i < header.length; i++)
      //     builder.append(header[i]);

      //   headers.put(header[0], builder.toString());
      //   content = reader.readNextLine();
      // };

      // try {
      //   byte[] bytes = new byte[headers.getLength()];
      //   reader.readFully(bytes);

      //   content = new String(bytes, StandardCharsets.UTF_8);
      //   if(request.isEmpty()) return new BadRequest();
        
      //   request.get().setHeaders(headers);
      //   return request.get().mount(content);
      // } catch (Exception e) {
      //   e.printStackTrace();
      //   return new BadRequest();
      // }
    } else throw new EOFException();
  };
};
