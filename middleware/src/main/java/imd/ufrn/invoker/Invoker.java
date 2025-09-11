package imd.ufrn.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import imd.ufrn.Marshaller;
import imd.ufrn.data.Response;
import imd.ufrn.data.Reader;
import lombok.Getter;
import imd.ufrn.data.errors.RemotingError;
import imd.ufrn.data.errors.InternalServerError;
import imd.ufrn.interceptors.Interceptor;
import imd.ufrn.interceptors.InvocationContext;

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
    
    try {
      for(Interceptor before : entry.before()) {
        before.intercept(context);
      };

      Object object = method.invoke(
        entry.instance(),
        params
      );

      if(object instanceof Response)
        context.setResult((Response<Object>) object);
    } catch (InvocationTargetException e) {
      if(e.getTargetException() instanceof RemotingError) {
        RemotingError error = (RemotingError) e.getTargetException();
        context.setResult(error.toResponse());
      };
    } catch (RemotingError e) {
      context.setResult(e.toResponse());
    } catch (Exception e) {};
    
    if(context.getResult() == null) {
      RemotingError error = new InternalServerError();
      context.setResult(error.toResponse());
    };

    for(Interceptor after : entry.after()) {
      after.intercept(context);
    };
    
    return context.getResult();
  };
};
