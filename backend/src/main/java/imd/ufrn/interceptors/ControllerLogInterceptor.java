package imd.ufrn.interceptors;

import java.util.UUID;

public class ControllerLogInterceptor implements Interceptor {
  private UUID uuid = UUID.randomUUID();

  @Override
  public void intercept(InvocationContext context) {
    System.out.println("[ControllerLogInterceptor] " + uuid.toString());
    System.out.println("[Controller] " + context.getKey().method() + " - " + context.getKey().path());
  };
};
