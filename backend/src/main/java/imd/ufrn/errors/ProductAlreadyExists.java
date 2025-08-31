package imd.ufrn.errors;

import imd.ufrn.data.StatusCode;
import imd.ufrn.data.errors.Error;

public class ProductAlreadyExists extends Error {
  public ProductAlreadyExists() {
    super(
      StatusCode.CONFLICT, 
      "Produto não encontrado!"
    );
  };
};
