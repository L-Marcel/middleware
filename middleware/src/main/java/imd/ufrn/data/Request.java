package imd.ufrn.data;

import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.core.JsonProcessingException;

import imd.ufrn.enums.Method;
import imd.ufrn.utils.Serialization;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class Request implements Content {
  public String path;
  public String[] params;
  public Method method;
  public Headers headers;

  public abstract Object getBody();

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
};
