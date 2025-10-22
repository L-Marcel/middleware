package imd.ufrn.lifecycle.strategies;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import imd.ufrn.lifecycle.Bean;
import imd.ufrn.lifecycle.LifecycleStrategy;

public class PerRequestLifecycle implements LifecycleStrategy {
  public static final Map<String, ConcurrentLinkedQueue<Bean>> poll = new ConcurrentHashMap<>();

  @Override
  public Bean activate(Bean bean) throws Exception {
    Queue<Bean> queue = PerRequestLifecycle.poll.computeIfAbsent(
      bean.getName(),
      (key) -> new ConcurrentLinkedQueue<>()
    );

    if(queue.isEmpty()) {
      synchronized(queue) {
        if(queue.isEmpty()) {
          for(int i = 0; i < 10; i++) {
            Bean cloned = bean.clone();
            cloned.activate();
            cloned.setActivated(false);
            queue.add(cloned);
          };
        };
      };
    };

    Bean cached = queue.poll();

    if(cached != null) {
      cached.setActivated(true);
      return cached;
    } else {
      Bean cloned = bean.clone();
      cloned.activate();
      queue.add(cloned);
      return cloned;
    }
  };

  @Override
  public void destroy(Bean bean) throws Exception {
    if(bean != null) {
      Queue<Bean> queue = PerRequestLifecycle.poll.get(
        bean.getName()
      );

      if(queue == null || queue.size() >= 10) {
        bean.setActivated(false);
        return;
      };

      bean.setActivated(false);
      queue.add(bean);
    };
  };
};