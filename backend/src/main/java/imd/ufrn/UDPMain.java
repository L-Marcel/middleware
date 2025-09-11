package imd.ufrn;

import java.time.Duration;

import imd.ufrn.controllers.ProductsController;
import imd.ufrn.enums.TransportProtocol;

public class UDPMain {
  public static void main(String[] args) {
    DawnSettings settings = new DawnSettings(
      TransportProtocol.UDP,
      8080,
      Duration.ofSeconds(1),
      false,
      0
    );

    Dawn dawn = new Dawn(settings);
    dawn.addController(ProductsController.class);
    dawn.start();
  };
};