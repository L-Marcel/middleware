package imd.ufrn.reflection;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import imd.ufrn.data.Headers;
import imd.ufrn.data.Request;
import imd.ufrn.data.Response;
import imd.ufrn.enums.Method;
import imd.ufrn.utils.Paths;

public class RequestMapping {
  private List<RequestMappingEntry> list;

  public RequestMapping() {
    this.list = new LinkedList<>();
  };

  public void register(
    Method method,
    String path,
    Class<Object> requestBodyClass,
    Class<Request<Object>> request,
    Class<Response<Object>> response
  ) {
    this.list.add(
      new RequestMappingEntry(
        method, 
        path,
        requestBodyClass,
        request,
        response
      )
    );
  };

  public Optional<Request<Object>> findRequest(Method method, String path) {
    return this.list.stream()
      .filter((request) -> request.method().equals(method))
      .map((mapping) -> {
        List<String> params = Paths.match(path, mapping.path());
        if(params == null) return null;
        
        try {
          Constructor<Request<Object>> constructor = mapping
            .request()
            .getDeclaredConstructor();
        
          Request<Object> request = constructor.newInstance(
            mapping.bodyClass()
          );

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
};
