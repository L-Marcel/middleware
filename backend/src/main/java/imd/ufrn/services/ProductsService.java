package imd.ufrn.services;

import java.util.List;

import imd.ufrn.errors.ProductAlreadyExists;
import imd.ufrn.errors.ProductNotFound;
import imd.ufrn.errors.ProductUnavailableToSell;
import imd.ufrn.models.Product;
import imd.ufrn.repositories.ProductsRepository;

public class ProductsService {
  private final ProductsRepository repository;
  public ProductsService() {
    this.repository = new ProductsRepository();
  };

  public Product findById(
    Integer id
  ) throws ProductNotFound {
    Product product = repository.getProducts().get(id);
    if(product == null) throw new ProductNotFound();
    return product;
  };

  public List<Product> findAll() {
    return this.repository
      .getProducts()
      .values()
      .stream()
      .toList();
  };

  public void register(
    Product product
  ) throws ProductAlreadyExists {
    Product created = this.repository
      .getProducts()
      .putIfAbsent(
        product.getId(), 
        product
      );

    if(created != null) 
      throw new ProductAlreadyExists();
  };

  public void sell(
    Integer id,
    Integer amount
  ) throws ProductUnavailableToSell {
    Product updated = this.repository
      .getProducts()
      .computeIfPresent(id, (key, product) -> {
        if(product.getAmount() < amount)
          throw new ProductUnavailableToSell();
      
        product.setAmount(product.getAmount() - amount);
        return product;
      });
    
    if(updated == null)
      throw new ProductNotFound();
  };

  public void buy(
    Integer id,
    Integer amount
  ) throws ProductUnavailableToSell {
    Product updated = this.repository
      .getProducts()
      .computeIfPresent(id, (key, product) -> {
        product.setAmount(product.getAmount() + amount);
        return product;
      });
    
    if(updated == null)
      throw new ProductNotFound();
  };

  public void delete(
    Integer id
  ) throws ProductUnavailableToSell {
    Product removed = this.repository
      .getProducts()
      .remove(id);
    
    if(removed == null)
      throw new ProductNotFound();
  };
};
