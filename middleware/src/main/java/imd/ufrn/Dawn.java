package imd.ufrn;

import imd.ufrn.reflection.ReflectionLookup;

public class Dawn {
  public void addController(Class<?> controller) {
    ReflectionLookup
      .getInstance()
      .add(controller);
  };
};