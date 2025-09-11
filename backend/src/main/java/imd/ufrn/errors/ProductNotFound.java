package imd.ufrn.errors;

import imd.ufrn.data.StatusCode;
import imd.ufrn.data.errors.RemotingError;

public class ProductNotFound extends RemotingError {
  public ProductNotFound() {
    super(
      StatusCode.NOT_FOUND, 
      "Produto não encontrado!"
    );
  };
};
