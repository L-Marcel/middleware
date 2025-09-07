package imd.ufrn.repositories;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import imd.ufrn.models.Product;
import lombok.Getter;

@Getter
public class ProductsRepository {
  private final Map<Integer, Product> products;
  public ProductsRepository() {
    this.products = new ConcurrentHashMap<>();
    this.mock();
  };

  private void mock() {
    this.products.put(0, new Product(0, "Notebook", BigDecimal.valueOf(3000), 3));
  };
};
