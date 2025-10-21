package imd.ufrn.errors;

import imd.ufrn.data.StatusCode;
import imd.ufrn.data.errors.RemoteError;

public class ProductAlreadyExists extends RemoteError {
  public ProductAlreadyExists() {
    super(
      StatusCode.CONFLICT, 
      "Produto já existe!"
    );
  };
};
