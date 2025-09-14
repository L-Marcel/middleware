package imd.ufrn.lifecycle;

import imd.ufrn.lifecycle.strategies.PerRequestLifecycle;
import imd.ufrn.lifecycle.strategies.StaticLifecycle;
import lombok.Getter;

public class LifecycleManager {
  @Getter
  private static final LifecycleManager instance = new LifecycleManager();

  public Bean activate(Bean bean) throws Exception {
    LifecycleStrategy strategy;

    switch (bean.getLifecycle()) {
      case STATIC:
        strategy = new StaticLifecycle();
        break;
      case PER_INSTANCE:
      default:
        strategy = new PerRequestLifecycle();
        break;
    };

    return strategy.activate(bean);
  };

  public void destroy(Bean bean) throws Exception {
    LifecycleStrategy strategy;

    switch (bean.getLifecycle()) {
      case STATIC:
        strategy = new StaticLifecycle();
        break;
      case PER_INSTANCE:
      default:
        strategy = new PerRequestLifecycle();
        break;
    };

    strategy.destroy(bean);
  };
};
