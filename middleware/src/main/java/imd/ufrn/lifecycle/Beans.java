package imd.ufrn.lifecycle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

public class Beans {
  @Getter
  private static final Beans instance = new Beans();
  private Map<String, Bean> beans = new ConcurrentHashMap<>();

  public void register(Bean bean) {
    this.beans.putIfAbsent(bean.getName(), bean);
  };

  public Bean get(String name) {
    return this.beans.get(name);
  };
};
