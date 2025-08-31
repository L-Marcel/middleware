package imd.ufrn.controllers;

import java.util.List;

import imd.ufrn.annotations.DeleteMapping;
import imd.ufrn.annotations.GetMapping;
import imd.ufrn.annotations.PostMapping;
import imd.ufrn.annotations.QueryParam;
import imd.ufrn.annotations.RequestBody;
import imd.ufrn.annotations.RestController;
import imd.ufrn.data.Response;
import imd.ufrn.dto.Amount;
import imd.ufrn.models.Product;
import imd.ufrn.services.ProductsService;

@RestController("products")
public class ProductsController {
  private final ProductsService service;
  public ProductsController() {
    this.service = new ProductsService();
  };

  @GetMapping
  public Response<List<Product>> findAll() {
    List<Product> products = this.service.findAll();
    return Response.ok(products);
  };

  @GetMapping("{id}")
  public Response<Product> findById(
    @QueryParam Integer id
  ) {
    Product product = this.service.findById(id);
    return Response.ok(product);
  };

  @PostMapping
  public Response<Void> register(
    @RequestBody Product product
  ) {
    this.service.register(product);
    return Response.created();
  };

  @PostMapping("{id}/buy")
  public Response<Void> buyById(
    @QueryParam Integer id,
    @RequestBody Amount amount
  ) {
    this.service.buy(id, amount.value());
    return Response.ok();
  };

  @PostMapping("{id}/sell")
  public Response<Void> sellById(
    @QueryParam Integer id,
    @RequestBody Amount amount
  ) {
    this.service.sell(id, amount.value());
    return Response.ok();
  };

  @DeleteMapping("{id}")
  public Response<Void> deleteById(
    @QueryParam Integer id
  ) {
    this.service.delete(id);
    return Response.ok();
  };
};
