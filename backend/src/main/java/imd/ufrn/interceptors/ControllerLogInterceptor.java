package imd.ufrn.interceptors;

public class ControllerLogInterceptor implements Interceptor {
  @Override
  public void intercept(InvocationContext context) {
    System.out.println("[Controller] " + context.getKey().method() + " - " + context.getKey().path());
  };
};
