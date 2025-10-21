package imd.ufrn.data.errors;

import imd.ufrn.data.Response;
import imd.ufrn.data.StatusCode;
import imd.ufrn.errors.ErrorPayload;
import lombok.Getter;

@Getter
public class RemoteError extends RuntimeException {
  private StatusCode code;

  public RemoteError(StatusCode code, String message) {
    super(message);
    this.code = code;
  };

  public Response<ErrorPayload> toResponse() {
    return Response.error(this);
  };
};
