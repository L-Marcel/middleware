package imd.ufrn.reflection;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import imd.ufrn.data.Headers;
import imd.ufrn.data.Request;
import imd.ufrn.enums.HttpMethod;
import imd.ufrn.utils.Paths;

public class Lookup {
  private List<LookupEntry> list;

  public Lookup() {
    this.list = new LinkedList<>();
  };

  public void register(
    LookupEntry entry
  ) {
    this.list.add(entry);
  };

  public Optional<Request<Object>> findRequest(HttpMethod method, String path) {
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
