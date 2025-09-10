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
    this.products.put(1, new Product(1, "Caderno", BigDecimal.valueOf(30), 10));
    this.products.put(2, new Product(2, "Livro", BigDecimal.valueOf(50), 30));
    this.products.put(3, new Product(3, "Papel", BigDecimal.valueOf(0.5), 100));
  };
};
