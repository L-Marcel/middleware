package imd.ufrn;

import imd.ufrn.handler.ServerRequestHandler;
public class Dawn {
  private ServerRequestHandler handler;

  public Dawn(DawnSettings settings) {
    this.handler = new ServerRequestHandler(settings);
  };

  public void addController(Class<?> controller) {
    Reflection
      .getInstance()
      .add(controller);
  };

  public void start() {
    Reflection
      .getInstance()
      .map();

    this.handler.start();
    try {
      this.handler.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  };
};