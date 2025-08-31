package imd.ufrn.data.errors;

import imd.ufrn.data.StatusCode;

public class BadRequest extends Error {
  public BadRequest() {
    super(
      StatusCode.BAD_REQUEST, 
      "Requisição inválida!"
    );
  };
};
