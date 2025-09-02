package imd.ufrn;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;

import imd.ufrn.annotations.DeleteMapping;
import imd.ufrn.annotations.GetMapping;
import imd.ufrn.annotations.MethodMapping;
import imd.ufrn.annotations.PathParam;
import imd.ufrn.annotations.PostMapping;
import imd.ufrn.annotations.PutMapping;
import imd.ufrn.annotations.RequestBody;
import imd.ufrn.annotations.RestController;
import imd.ufrn.enums.HttpMethod;
import imd.ufrn.errors.AnnotationNotPresent;
import imd.ufrn.lookup.Lookup;
import imd.ufrn.lookup.LookupEntry;
import imd.ufrn.lookup.LookupEntryBody;
import imd.ufrn.lookup.LookupEntryParam;
import imd.ufrn.lookup.LookupKey;
import lombok.Getter;

public class Reflection {
  @Getter
  private static final Reflection instance = new Reflection();
  private static final List<Class<? extends Annotation>> annotations = List.of(
    GetMapping.class,
    PostMapping.class,
    PutMapping.class,
    DeleteMapping.class
  );

  private List<Class<?>> controllers;
  
  private Reflection() {
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

  public void map() {
    for(Class<?> controller : this.controllers) {
      RestController annotation = controller
        .getAnnotation(RestController.class);
      
      System.out.println("Registering controler: " + annotation.value());
      for(Method method : controller.getMethods()) {
        this.mapRemotes(annotation.value(), method);
      };
    };
  };

  public void mapRemotes(String root, Method remote) {
    for(Class<? extends Annotation> annotation : annotations) {
      if(remote.isAnnotationPresent(annotation)) {
        try {
          String path = root + "/" + this.extractMethodRequestPath(
            remote, 
            annotation
          );

          HttpMethod httpMethod = annotation
            .getAnnotation(MethodMapping.class)
            .value();
          
          LookupKey key = new LookupKey(httpMethod, path);
          LookupEntryBody body = new LookupEntryBody();
          List<LookupEntryParam> params = new LinkedList<>(); 
          
          for(Parameter param : remote.getParameters()) {
            if(param.isAnnotationPresent(RequestBody.class)) {
              body = new LookupEntryBody(param.getType());
            } else if(param.isAnnotationPresent(PathParam.class)) {
              String name = param.getAnnotation(PathParam.class).value();

              params.add(
                new LookupEntryParam(
                  name, 
                  param.getType()
                )
              );
            };
          };

          Lookup
            .getInstance()
            .register(
              new LookupEntry(
                key,
                body,
                params,
                remote
              )
            );

          break;
        } catch (Exception e) {
          e.printStackTrace();
          break;
        }
      };
    };
  };

  public String extractMethodRequestPath(
    Method method,
    Class<? extends Annotation> annotation
  ) throws Exception {
    Annotation instance = method.getAnnotation(annotation);
    return (String) annotation.getMethod("value").invoke(instance);
  };
};
