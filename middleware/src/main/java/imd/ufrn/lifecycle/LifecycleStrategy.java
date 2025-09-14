package imd.ufrn.lifecycle;

public interface LifecycleStrategy {
  public Bean activate(Bean bean) throws Exception ;
  public void destroy(Bean bean) throws Exception ;
};
