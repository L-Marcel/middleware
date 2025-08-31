package imd.ufrn.errors;

import imd.ufrn.data.StatusCode;
import imd.ufrn.data.errors.Error;

public class ProductNotFound extends Error {
  public ProductNotFound() {
    super(
      StatusCode.NOT_FOUND, 
      "Produto não encontrado!"
    );
  };
};
