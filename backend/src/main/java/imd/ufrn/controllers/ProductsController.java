package imd.ufrn.controllers;

import java.util.List;

import imd.ufrn.annotations.DeleteMapping;
import imd.ufrn.annotations.GetMapping;
import imd.ufrn.annotations.InterceptAfter;
import imd.ufrn.annotations.InterceptBefore;
import imd.ufrn.annotations.PathParam;
import imd.ufrn.annotations.PostMapping;
import imd.ufrn.annotations.RequestBody;
import imd.ufrn.annotations.RestController;
import imd.ufrn.data.Response;
import imd.ufrn.dto.Amount;
import imd.ufrn.interceptors.ControllerLogInterceptor;
import imd.ufrn.interceptors.RemoteLogInterceptor;
import imd.ufrn.interceptors.StatusLogInterceptor;
import imd.ufrn.models.Product;
import imd.ufrn.services.ProductsService;

@RestController("products")
@InterceptBefore({ControllerLogInterceptor.class})
public class ProductsController {
  private final ProductsService service;
  public ProductsController() {
    this.service = new ProductsService();
  };

  @GetMapping
  @InterceptAfter({StatusLogInterceptor.class})
  public Response<List<Product>> findAll() {
    List<Product> products = this.service.findAll();
    return Response.ok(products);
  };

  @GetMapping("{id}")
  public Response<Product> findById(
    @PathParam("id") Integer id
  ) {
    Product product = this.service.findById(id);
    return Response.ok(product);
  };

  @PostMapping
  @InterceptAfter({StatusLogInterceptor.class})
  public Response<Void> register(
    @RequestBody Product product
  ) {
    this.service.register(product);
    return Response.created();
  };

  @PostMapping("{id}/buy")
  @InterceptBefore({RemoteLogInterceptor.class})
  public Response<Void> buyById(
    @PathParam("id") Integer id,
    @RequestBody Amount amount
  ) {
    this.service.buy(id, amount.value());
    return Response.ok();
  };

  @PostMapping("{id}/sell")
  @InterceptBefore({RemoteLogInterceptor.class})
  @InterceptAfter({StatusLogInterceptor.class})
  public Response<Void> sellById(
    @PathParam("id") Integer id,
    @RequestBody Amount amount
  ) {
    this.service.sell(id, amount.value());
    return Response.ok();
  };

  @DeleteMapping("{id}")
  public Response<Void> deleteById(
    @PathParam("id") Integer id
  ) {
    this.service.delete(id);
    return Response.ok();
  };
};
