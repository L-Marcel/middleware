package imd.ufrn.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import imd.ufrn.Marshaller;
import imd.ufrn.beans.ContextInterceptor;
import imd.ufrn.data.Response;
import imd.ufrn.data.Reader;
import lombok.Getter;
import imd.ufrn.data.errors.RemoteError;
import imd.ufrn.data.errors.InternalServerError;
import imd.ufrn.interceptors.InvocationContext;
import imd.ufrn.lifecycle.Bean;
import imd.ufrn.lifecycle.Beans;
import imd.ufrn.lifecycle.LifecycleManager;

public class Invoker {
  @Getter
  private static final Invoker instance = new Invoker();

  @SuppressWarnings("unchecked")
  public Response<? extends Object> invoke(
    InvocationContext context,
    Reader reader,
    InvokerEntry entry
  ) throws Exception {
    Method method = entry.remote();

    Object[] params = entry
      .params()
      .stream()
      .map((param) -> {
        try {
          if(param.body()) {
            return Marshaller
              .getInstance()
              .mount(reader, param.type());
          };

          return Marshaller
            .getInstance()
            .deserialize(
              param.value(), 
              param.type()
            );
        } catch (Exception e) {
          return null;
        }
      }).toArray();

    context.setParams(params);
    
    Bean bean = null;
    try {
      bean =  Beans
        .getInstance()
        .get(entry.controller());

      bean = LifecycleManager
        .getInstance()
        .activate(bean);

      for(String before : entry.before()) {
        ContextInterceptor interceptor = (ContextInterceptor) Beans
          .getInstance()
          .get(before);

        interceptor = (ContextInterceptor) LifecycleManager
          .getInstance()
          .activate(interceptor);

        interceptor
          .getInterceptor()
          .intercept(context);
      };
      
      Object controller = (Object) bean.getInstance();
      Object object = method.invoke(
        controller,
        params
      );

      if(object instanceof Response)
        context.setResult((Response<Object>) object);
    } catch (InvocationTargetException e) {
      if(e.getTargetException() instanceof RemoteError) {
        RemoteError error = (RemoteError) e.getTargetException();
        context.setResult(error.toResponse());
      };
    } catch (RemoteError e) {
      context.setResult(e.toResponse());
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      LifecycleManager
        .getInstance()
        .destroy(bean);
    };
    
    if(context.getResult() == null) {
      RemoteError error = new InternalServerError();
      context.setResult(error.toResponse());
    };

    for(String after : entry.after()) {
      ContextInterceptor interceptor = (ContextInterceptor) Beans
        .getInstance()
        .get(after);

      interceptor = (ContextInterceptor) LifecycleManager
        .getInstance()
        .activate(interceptor);

      interceptor
        .getInterceptor()
        .intercept(context);
    };
    
    return context.getResult();
  };
};
