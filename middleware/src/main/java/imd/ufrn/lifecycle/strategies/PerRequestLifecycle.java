package imd.ufrn.lifecycle.strategies;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import imd.ufrn.lifecycle.Bean;
import imd.ufrn.lifecycle.LifecycleStrategy;

public class PerRequestLifecycle implements LifecycleStrategy {
  public static final Map<String, List<Bean>> poll = new ConcurrentHashMap<>();

  @Override
  public Bean activate(Bean bean) throws Exception {
    List<Bean> poll = PerRequestLifecycle.poll.computeIfAbsent(
      bean.getName(),
      (key) -> Collections.synchronizedList(new LinkedList<>())
    );

    synchronized(poll) {
      for(Bean cached : poll) {
        synchronized(cached) {
          if(!cached.isActivated()) {
            cached.setActivated(true);
            return cached;
          };
        };
      };

      if(poll.isEmpty()) {
        for(int i = 0; i < 10; i++) {
          Bean cloned = bean.clone();
          cloned.activate();
          cloned.setActivated(false);
          poll.add(cloned);
        };

        Bean first = poll.getFirst();
        first.setActivated(true);
        return first;
      } else {
        Bean cloned = bean.clone();
        cloned.activate();
        poll.add(cloned);
        return cloned;
      }
    }
  };

  @Override
  public void destroy(Bean bean) throws Exception {
    if(bean != null) {
      List<Bean> poll = PerRequestLifecycle.poll.getOrDefault(
        bean.getName(),
        Collections.synchronizedList(new LinkedList<>())
      );

      synchronized(poll) {
        if(poll.size() >= 10) {
          poll.remove(bean);
        } else {
          synchronized(bean) {
            bean.setActivated(false);
          };
        };
      };
    };
  };
};