package imd.ufrn.interceptors;

public class RemoteLogInterceptor implements Interceptor {
  @Override
  public void intercept(InvocationContext context) {
    System.out.println("[Remote] " + context.getInvoke().getClass().getName());
  };
};
