package imd.ufrn.data.errors;

import imd.ufrn.data.Response;
import imd.ufrn.data.StatusCode;
import imd.ufrn.errors.ErrorPayload;
import lombok.Getter;

@Getter
public class RemotingError extends RuntimeException {
  private StatusCode code;

  public RemotingError(StatusCode code, String message) {
    super(message);
    this.code = code;
  };

  public Response<ErrorPayload> toResponse() {
    return Response.error(this);
  };
};
