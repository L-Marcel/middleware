package imd.ufrn.errors;

import imd.ufrn.data.StatusCode;
import imd.ufrn.data.errors.RemoteError;

public class ProductUnavailableToSell extends RemoteError {
  public ProductUnavailableToSell() {
    super(
      StatusCode.BAD_REQUEST, 
      "Produto indisponível para venda!"
    );
  };
};
