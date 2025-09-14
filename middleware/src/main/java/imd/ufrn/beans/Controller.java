package imd.ufrn.beans;

import imd.ufrn.lifecycle.Bean;
import imd.ufrn.lifecycle.Lifecycle;

public class Controller extends Bean {
  public Controller(Class<? extends Object> _class) {
    super(_class, Lifecycle.STATIC);
  };

  @Override
  public Bean clone() {
    return new Controller(this._class);
  };
};
