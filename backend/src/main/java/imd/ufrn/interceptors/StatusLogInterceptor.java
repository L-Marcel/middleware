package imd.ufrn.interceptors;

import imd.ufrn.data.Response;

public class StatusLogInterceptor implements Interceptor {
  @Override
  public void intercept(InvocationContext context) {
    if(context.getResult() instanceof Response) {
      Response<?> response = (Response<?>) context.getResult();
      try {
        System.out.println("[Response]\n\n" + response.serialize() + "\n");
      } catch (Exception e) {};
    };
  };
};
