package imd.ufrn.reflection;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import imd.ufrn.data.Headers;
import imd.ufrn.data.Request;
import imd.ufrn.enums.Method;
import imd.ufrn.utils.Paths;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class RequestMapping {
  private List<Mapping> list;

  public RequestMapping() {
    this.list = new LinkedList<>();
  };

  public void register(
    Method method,
    String path,
    Class<? extends Request> request
  ) {
    this.list.add(
      new Mapping(
        method, 
        path, 
        request
      )
    );
  };

  public Optional<Request> findRequest(Method method, String path) {
    return this.list.stream()
      .filter((request) -> request.getMethod().equals(method))
      .map((mapping) -> {
        List<String> params = Paths.match(path, mapping.path);
        if(params == null) return null;

        try {
          Constructor<? extends Request> constructor = mapping
            .getRequest()
            .getDeclaredConstructor();
        
          Request request = constructor.newInstance();
          Headers headers = new Headers();
          request.setHeaders(headers);
          request.setMethod(method);
          request.setPath(path);
          request.setParams(
            params.stream()
              .toArray(String[]::new)
          );
          
          return request;
        } catch (Exception e) {
          return null;
        }
      }).filter((_class) -> _class != null)
      .findFirst();
  };

  @AllArgsConstructor
  @Getter
  public class Mapping {
    private Method method;
    private String path;
    private Class<? extends Request> request;
  };
};
