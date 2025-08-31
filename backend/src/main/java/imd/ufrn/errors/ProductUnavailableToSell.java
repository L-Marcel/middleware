package imd.ufrn.errors;

import imd.ufrn.data.StatusCode;
import imd.ufrn.data.errors.Error;

public class ProductUnavailableToSell extends Error {
  public ProductUnavailableToSell() {
    super(
      StatusCode.BAD_REQUEST, 
      "Produto indisponível para venda!"
    );
  };
};
