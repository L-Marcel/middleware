package imd.ufrn.data.errors;

import imd.ufrn.data.Response;
import imd.ufrn.data.StatusCode;
import lombok.Getter;

@Getter
public class Error extends RuntimeException {
  private StatusCode code;

  public Error(StatusCode code, String message) {
    super(message);
    this.code = code;
  };

  public Response<Error> toResponse() {
    return Response.error(this);
  };
};
