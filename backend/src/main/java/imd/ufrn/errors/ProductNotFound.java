package imd.ufrn.errors;

import imd.ufrn.data.StatusCode;
import imd.ufrn.data.errors.RemoteError;

public class ProductNotFound extends RemoteError {
  public ProductNotFound() {
    super(
      StatusCode.NOT_FOUND, 
      "Produto não encontrado!"
    );
  };
};
