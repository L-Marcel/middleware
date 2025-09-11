package imd.ufrn.data.errors;

import imd.ufrn.data.StatusCode;

public class BadRequest extends RemotingError {
  public BadRequest() {
    super(
      StatusCode.BAD_REQUEST, 
      "Requisição inválida!"
    );
  };
};
