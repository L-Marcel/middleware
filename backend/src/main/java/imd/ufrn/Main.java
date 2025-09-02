package imd.ufrn;

import imd.ufrn.controllers.ProductsController;

public class Main {
  public static void main(String[] args) {
    Dawn dawn = new Dawn();
    dawn.addController(ProductsController.class);
    dawn.start();
  };
};