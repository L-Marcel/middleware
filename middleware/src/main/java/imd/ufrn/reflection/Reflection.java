package imd.ufrn.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        this.mapMethod(annotation.value(), method);
      };
    };
  };

  public void mapMethod(String root, Method method) {
    for(Class<? extends Annotation> annotation : annotations) {
      if(method.isAnnotationPresent(annotation)) {
        try {
          String path = root + "/" + this.extractMethodRequestPath(
            method, 
            annotation
          );

          HttpMethod httpMethod = annotation
            .getAnnotation(MethodMapping.class)
            .value();
          
          Map<String, Class<?>> params = new LinkedHashMap<>(); 
          Class<?> body = null;
          for(Parameter param : method.getParameters()) {
            if(param.isAnnotationPresent(RequestBody.class)) {
              body = param.getType();
            } else if(param.isAnnotationPresent(PathParam.class)) {
              String name = param.getAnnotation(PathParam.class).value();

              params.put(
                name, 
                param.getType()
              );
            };
          };

          System.out.println("Registering path: " + httpMethod + " " + path);
          System.out.println("with " + params.size() + " params ");
          if(body != null) System.out.println("and body " + body.toString());
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
