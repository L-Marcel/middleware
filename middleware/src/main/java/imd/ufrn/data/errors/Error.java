package imd.ufrn.data.errors;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.JsonProcessingException;

import imd.ufrn.data.Content;
import imd.ufrn.data.StatusCode;
import imd.ufrn.utils.Serialization;
import lombok.Getter;

@Getter
public class Error extends RuntimeException 
implements Content {
  private StatusCode code;

  public Error(StatusCode code, String message) {
    super(message);
    this.code = code;
  };

  @Override
  public String serialize() throws JsonProcessingException {
    String body = Serialization.serialize(this);
    
    return String.format(
      """
      HTTP/1.1 %d %s
      Content-Type: application/json
      Content-Length: %d
      
      %s
      """, 
      this.getCode().getValue(), 
      this.getCode().getName(),
      body.getBytes(StandardCharsets.UTF_8).length,
      body
    );
  }

  @Override
  public Content mount(String body) throws IOException, JsonProcessingException {
    return this;
  };
};
