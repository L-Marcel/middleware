package imd.ufrn.errors;

import imd.ufrn.data.StatusCode;
import imd.ufrn.data.errors.RemotingError;

public class ProductUnavailableToSell extends RemotingError {
  public ProductUnavailableToSell() {
    super(
      StatusCode.BAD_REQUEST, 
      "Produto indisponível para venda!"
    );
  };
};
