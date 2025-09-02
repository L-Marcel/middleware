package imd.ufrn.data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;

import imd.ufrn.enums.HttpMethod;
import imd.ufrn.utils.Serialization;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request implements Content {
  public final Class<? extends Object> bodyClass;
  public Object body;
  public String path;
  public String[] params;
  public HttpMethod method;
  public Headers headers;

  public Request(Class<? extends Object> bodyClass) {
    this.bodyClass = bodyClass;
  };

  @Override
  public String serialize() throws JsonProcessingException {
    if(this.getBody() == null) {
      return String.format(
        """
        %s %s HTTP/1.1
        Content-Type: application/json
        Content-Length: 0
        """,
        this.getMethod(),
        this.getPath()
      ).replace("\n", "\r\n");
    } else {
      String body = Serialization.serialize(this.getBody());

      return String.format(
        """
        %s %s HTTP/1.1
        Content-Type: application/json
        Content-Length: %d
        
        %s""",
        this.getMethod(),
        this.getPath(),
        body.getBytes(StandardCharsets.UTF_8).length,
        body
      ).replace("\n", "\r\n");
    }
  };

  @Override
  public Content mount(String body) throws IOException, JsonProcessingException {
    Optional<?> _body = Serialization.deserialize(
      body, 
      this.bodyClass
    );

    if(_body.isPresent()) {
      this.setBody(_body.get());
      return this;
    } else {
      return this;
    }
  };
};
