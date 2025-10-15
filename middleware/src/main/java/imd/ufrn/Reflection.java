package imd.ufrn;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;

import imd.ufrn.annotations.DeleteMapping;
import imd.ufrn.annotations.GetMapping;
import imd.ufrn.annotations.InterceptAfter;
import imd.ufrn.annotations.InterceptBefore;
import imd.ufrn.annotations.MethodMapping;
import imd.ufrn.annotations.PathParam;
import imd.ufrn.annotations.PostMapping;
import imd.ufrn.annotations.PutMapping;
import imd.ufrn.annotations.RequestBody;
import imd.ufrn.annotations.RestController;
import imd.ufrn.beans.ContextInterceptor;
import imd.ufrn.beans.Controller;
import imd.ufrn.enums.HttpMethod;
import imd.ufrn.errors.AnnotationNotPresent;
import imd.ufrn.interceptors.Interceptor;
import imd.ufrn.lookup.Lookup;
import imd.ufrn.lookup.LookupEntry;
import imd.ufrn.lookup.LookupEntryParam;
import imd.ufrn.lookup.LookupKey;
import imd.ufrn.lifecycle.Bean;
import imd.ufrn.lifecycle.Beans;
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
    try {
      for(Class<?> controller : this.controllers) {
        RestController annotation = controller
          .getAnnotation(RestController.class);

        Bean bean = new Controller(controller);
        Beans.getInstance().register(bean);

        List<String> before = new LinkedList<String>();
        if(controller.isAnnotationPresent(InterceptBefore.class)) {
          InterceptBefore interceptBefore = controller.getAnnotation(InterceptBefore.class);
          for(Class<? extends Interceptor> interceptor : interceptBefore.value()) {
            before.add(interceptor.getName());
            ContextInterceptor contextInterceptor = new ContextInterceptor(interceptor);
            Beans.getInstance().register(contextInterceptor);
          };
        };

        List<String> after = new LinkedList<String>();
        if(controller.isAnnotationPresent(InterceptAfter.class)) {
          InterceptAfter interceptAfter = controller.getAnnotation(InterceptAfter.class);
          for(Class<? extends Interceptor> interceptor : interceptAfter.value()) {
            after.add(interceptor.getName());
            ContextInterceptor contextInterceptor = new ContextInterceptor(interceptor);
            Beans.getInstance().register(contextInterceptor);
          };
        };

        System.out.println("[Server] Registering controler: " + annotation.value());
        for(Method method : controller.getMethods()) {
          this.mapRemotes(
            annotation.value(), 
            method,
            new LinkedList<>(before),
            new LinkedList<>(after),
            controller.getName()
          );
        };
      };
    } catch (Exception e) {
      e.printStackTrace();
    }
  };

  public void mapRemotes(
    String root, 
    Method remote, 
    List<String> before,
    List<String> after,
    String controller
  ) {
    for(Class<? extends Annotation> annotation : annotations) {
      if(remote.isAnnotationPresent(annotation)) {
        try {
          String methodPath = this.extractMethodRequestPath(
            remote, 
            annotation
          );

          String path = "/" + root;
          if(!methodPath.isEmpty())
            path += "/" + methodPath;

          HttpMethod httpMethod = annotation
            .getAnnotation(MethodMapping.class)
            .value();
          
          LookupKey key = new LookupKey(httpMethod, path);
          List<LookupEntryParam> params = new LinkedList<>(); 
          
          for(Parameter param : remote.getParameters()) {
            if(param.isAnnotationPresent(RequestBody.class)) {
              params.add(
                new LookupEntryParam(
                  null,
                  true, 
                  param.getType(),
                  instance
                )
              );
            } else if(param.isAnnotationPresent(PathParam.class)) {
              String name = param.getAnnotation(PathParam.class).value();

              params.add(
                new LookupEntryParam(
                  name,
                  false,
                  param.getType(),
                  instance
                )
              );
            };
          };

          if(remote.isAnnotationPresent(InterceptBefore.class)) {
            InterceptBefore interceptBefore = remote.getAnnotation(InterceptBefore.class);
            for(Class<? extends Interceptor> interceptor : interceptBefore.value()) {
              before.add(interceptor.getName());
              ContextInterceptor contextInterceptor = new ContextInterceptor(interceptor);
              Beans.getInstance().register(contextInterceptor);
            };
          };

          if(remote.isAnnotationPresent(InterceptAfter.class)) {
            InterceptAfter interceptAfter = remote.getAnnotation(InterceptAfter.class);
            for(Class<? extends Interceptor> interceptor : interceptAfter.value()) {
              after.add(interceptor.getName());
              ContextInterceptor contextInterceptor = new ContextInterceptor(interceptor);
              Beans.getInstance().register(contextInterceptor);
            };
          };

          Lookup
            .getInstance()
            .register(
              new LookupEntry(
                key,
                before,
                after,
                params,
                controller,
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
