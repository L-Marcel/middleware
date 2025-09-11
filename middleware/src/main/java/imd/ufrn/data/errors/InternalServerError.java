package imd.ufrn.data.errors;

import imd.ufrn.data.StatusCode;

public class InternalServerError extends RemotingError {
  public InternalServerError() {
    super(
      StatusCode.INTERNAL_SERVER_ERROR, 
      "Erro interno no servidor!"
    );
  };
};
