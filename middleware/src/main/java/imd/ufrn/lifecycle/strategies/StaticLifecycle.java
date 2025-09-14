package imd.ufrn.lifecycle.strategies;

import imd.ufrn.lifecycle.Bean;
import imd.ufrn.lifecycle.LifecycleStrategy;

public class StaticLifecycle implements LifecycleStrategy {
  @Override
  public Bean activate(Bean bean) throws Exception {
    if(!bean.isActivated())
      bean.activate();

    return bean;
  };

  @Override
  public void destroy(Bean bean) throws Exception {};
};