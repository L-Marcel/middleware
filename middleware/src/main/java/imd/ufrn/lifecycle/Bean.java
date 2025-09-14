package imd.ufrn.lifecycle;

import java.lang.reflect.Constructor;

import lombok.Getter;
import lombok.Setter;

public abstract class Bean {
  protected Class<? extends Object> _class;

  @Getter
  private Lifecycle lifecycle;

  @Getter
  private Object instance;

  @Getter
  @Setter
  private boolean activated = false;

  public Bean(
    Class<? extends Object> _class,
    Lifecycle lifecycle
  ) {
    this._class = _class;
    this.lifecycle = lifecycle;
  };

  public void activate() throws Exception {
    Constructor<? extends Object> constructor = this._class.getConstructor();
    this.instance = constructor.newInstance();
    this.activated = true;
  };

  public String getName() {
    return this._class.getName();
  };

  public abstract Bean clone();
};
