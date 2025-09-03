package imd.ufrn.invoker;

import java.util.Optional;

import imd.ufrn.Marshaller;
import imd.ufrn.data.Response;
import imd.ufrn.data.connection.Reader;
import lombok.Getter;

public class Invoker {
  @Getter
  private static final Invoker instance = new Invoker();

  @SuppressWarnings("unchecked")
  public Response<Object> invoke(
    Reader reader,
    InvokerEntry entry
  ) throws Exception {
    Object result = entry.remote().invoke(
      entry
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
        })
    );

    if(result instanceof Response)
      return (Response<Object>) result;
    
    return null;
  };
};
