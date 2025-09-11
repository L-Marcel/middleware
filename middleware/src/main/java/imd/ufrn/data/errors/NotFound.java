package imd.ufrn.data.errors;

import imd.ufrn.data.StatusCode;

public class NotFound extends RemotingError {
  public NotFound() {
    super(
      StatusCode.NOT_FOUND, 
      "Recurso não encontrado!"
    );
  };
};
