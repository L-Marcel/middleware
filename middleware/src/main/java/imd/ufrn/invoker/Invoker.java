package imd.ufrn.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import imd.ufrn.Marshaller;
import imd.ufrn.data.Response;
import imd.ufrn.data.Reader;
import lombok.Getter;
import imd.ufrn.data.errors.Error;

public class Invoker {
  @Getter
  private static final Invoker instance = new Invoker();

  @SuppressWarnings("unchecked")
  public Response<Object> invoke(
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

    try {
      Object result = method.invoke(
        entry.instance(),
        params
      );

      if(result instanceof Response)
        return (Response<Object>) result;
    } catch (InvocationTargetException e) {
      if(e.getTargetException() instanceof Error) {
        throw (Error) e.getTargetException();
      };
    };
    
    return null;
  };
};
