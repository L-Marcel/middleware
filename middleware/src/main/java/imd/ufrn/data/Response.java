package imd.ufrn.data;

import java.nio.charset.StandardCharsets;

import imd.ufrn.data.errors.Error;
import imd.ufrn.errors.ErrorPayload;

import com.fasterxml.jackson.core.JsonProcessingException;

import imd.ufrn.utils.Serialization;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response<T extends Object> {
  private StatusCode code;
  private T body;

  public static <T> Response<T> ok(T body) {
    return new Response<>(StatusCode.OK, body);
  };

  public static <T> Response<T> ok() {
    return new Response<>(StatusCode.OK, null);
  };

  public static <T> Response<T> created(T body) {
    return new Response<>(StatusCode.CREATED, body);
  };

  public static <T> Response<T> created() {
    return new Response<>(StatusCode.CREATED, null);
  };

  public static Response<ErrorPayload> error(Error error) {
    return new Response<>(
      error.getCode(), 
      new ErrorPayload(
        error.getCode(),
        error.getMessage()
      )
    );
  };

  protected Response(StatusCode code) {
    this.code = code;
  };
  
  protected Response(StatusCode code, T body) {
    this.code = code;
    this.body = body;
  };

  public String serialize() throws JsonProcessingException {
    if(this.getBody() == null) {
      return String.format(
        """
        HTTP/1.1 %d %s
        Content-Type: application/json
        Content-Length: 0
        """, 
        this.getCode().getValue(), 
        this.getCode().getName()
      );
    } else {
      String body = Serialization.serialize(this.getBody());

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
  };
};
