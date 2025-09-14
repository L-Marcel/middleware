package imd.ufrn.beans;

import imd.ufrn.interceptors.Interceptor;
import imd.ufrn.lifecycle.Bean;
import imd.ufrn.lifecycle.Lifecycle;

public class ContextInterceptor extends Bean {
  public ContextInterceptor(Class<? extends Interceptor> _class) {
    super(_class, Lifecycle.PER_INSTANCE);
  };

  public Interceptor getInterceptor() {
    return (Interceptor) this.getInstance();
  };
  
  @Override
  @SuppressWarnings("unchecked")
  public Bean clone() {
    return new ContextInterceptor(
      (Class<? extends Interceptor>) this._class
    );
  };
};
