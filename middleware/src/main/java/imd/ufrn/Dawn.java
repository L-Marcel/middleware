package imd.ufrn;

import imd.ufrn.reflection.Reflection;

public class Dawn {
  public void addController(Class<?> controller) {
    Reflection
      .getInstance()
      .add(controller);
  };

  public void start() {
    Reflection
      .getInstance()
      .map();
  };
};