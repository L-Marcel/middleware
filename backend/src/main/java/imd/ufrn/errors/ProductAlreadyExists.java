package imd.ufrn.errors;

import imd.ufrn.data.StatusCode;
import imd.ufrn.data.errors.RemotingError;

public class ProductAlreadyExists extends RemotingError {
  public ProductAlreadyExists() {
    super(
      StatusCode.CONFLICT, 
      "Produto já existe!"
    );
  };
};
