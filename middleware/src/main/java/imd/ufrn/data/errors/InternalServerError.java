package imd.ufrn.data.errors;

import imd.ufrn.data.StatusCode;

public class InternalServerError extends RemoteError {
  public InternalServerError() {
    super(
      StatusCode.INTERNAL_SERVER_ERROR, 
      "Erro interno no servidor!"
    );
  };
};
