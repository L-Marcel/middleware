package imd.ufrn.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import imd.ufrn.Marshaller;
import imd.ufrn.data.Response;
import imd.ufrn.data.Reader;
import lombok.Getter;
import imd.ufrn.data.errors.Error;
import imd.ufrn.data.errors.InternalServerError;
import imd.ufrn.interceptors.Interceptor;
import imd.ufrn.interceptors.InvocationContext;

public class Invoker {
  @Getter
  private static final Invoker instance = new Invoker();

  @SuppressWarnings("unchecked")
  public Response<Object> invoke(
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
    
    Object result = null;
    
    try {
      for(Interceptor before : entry.before()) {
        before.intercept(context);
      };

      result = method.invoke(
        entry.instance(),
        params
      );

      context.setResult(result);
    } catch (InvocationTargetException e) {
      if(e.getTargetException() instanceof Error) {
        Error error = (Error) e.getTargetException();
        result = error.toResponse();
        context.setResult(result);
      } else {
        result = new InternalServerError().toResponse();
      };
    } catch (Exception e) {
      result = new InternalServerError().toResponse();
    };
    
    for(Interceptor after : entry.after()) {
      after.intercept(context);
    };
    
    if(result instanceof Response)
      return (Response<Object>) result;
    
    result = new InternalServerError().toResponse();
    return (Response<Object>) result;
  };
};
