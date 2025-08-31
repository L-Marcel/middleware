package imd.ufrn.handler.listeners.implementations;

import java.io.EOFException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import imd.ufrn.data.Content;
import imd.ufrn.data.Headers;
import imd.ufrn.data.Request;
import imd.ufrn.data.connection.Connection;
import imd.ufrn.data.connection.Reader;
import imd.ufrn.data.errors.BadRequest;
import imd.ufrn.data.errors.InternalServerError;
import imd.ufrn.enums.ApplicationProtocol;
import imd.ufrn.enums.Method;
import imd.ufrn.reflection.ReflectionLookup;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConcreteApplicationImplementation implements ApplicationImplementation {
  @Override
  public ApplicationProtocol getProtocol() {
    return ApplicationProtocol.HTTP;
  };

  @Override
  public Content read(String content) throws Exception {
    // TODO - Implementar versão do UDP
    return new InternalServerError();
  };

  @Override
  public Content read(Connection connection) throws Exception {
    Reader reader = connection.getReader();
    String content = reader.readNextLine();

    if(content.endsWith("HTTP/1.1")) {
      String[] parts = content.split(" ");
      if(parts.length < 2) return new BadRequest();

      String method = parts[0];
      String path = parts[1];
      
      Headers headers = new Headers();
      Optional<Request> request = ReflectionLookup
        .getInstance()
        .getMapping()
        .findRequest(
          Method.valueOf(method),
          path
        );

      content = reader.readNextLine();
      while(!content.trim().isEmpty()) {
        String[] header = content.split(": ");

        StringBuilder builder = new StringBuilder();
        for(int i = 1; i < header.length; i++)
          builder.append(header[i]);

        headers.put(header[0], builder.toString());
        content = reader.readNextLine();
      };

      try {
        byte[] bytes = new byte[headers.getLength()];
        reader.readFully(bytes);

        content = new String(bytes, StandardCharsets.UTF_8);
        if(request.isEmpty()) return new BadRequest();
        
        request.get().setHeaders(headers);
        return request.get().mount(content);
      } catch (Exception e) {
        e.printStackTrace();
        return new BadRequest();
      }
    } else throw new EOFException();
  };
};
