package imd.ufrn.repositories;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import imd.ufrn.models.Product;
import lombok.Getter;

@Getter
public class ProductsRepository {
  private final Map<Integer, Product> products;
  public ProductsRepository() {
    this.products = new ConcurrentHashMap<>();
  };
};
