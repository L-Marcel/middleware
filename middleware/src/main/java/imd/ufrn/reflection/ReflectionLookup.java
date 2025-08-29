package imd.ufrn.reflection;

import java.util.LinkedList;
import java.util.List;

import imd.ufrn.annotations.RestController;
import imd.ufrn.errors.AnnotationNotPresent;

public class ReflectionLookup {
  private static ReflectionLookup instance;
  public static ReflectionLookup getInstance() {
    if(ReflectionLookup.instance == null) {
      ReflectionLookup.instance = new ReflectionLookup();
    };

    return ReflectionLookup.instance;
  };

  public List<Class<?>> controllers;

  private ReflectionLookup() {
    this.controllers = new LinkedList<>();
  };

  public void add(Class<?> controller) throws AnnotationNotPresent {
    if(controller.isAnnotationPresent(RestController.class)) {
      this.controllers.add(controller);
    } else {
      throw new AnnotationNotPresent(
        "RestController annotation not present in " + 
        controller.getName() + 
        "!"
      );
    };
  };
};
