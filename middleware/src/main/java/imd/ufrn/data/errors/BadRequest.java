package imd.ufrn.data.errors;

import imd.ufrn.data.StatusCode;

public class BadRequest extends RemoteError {
  public BadRequest() {
    super(
      StatusCode.BAD_REQUEST, 
      "Requisição inválida!"
    );
  };
};
