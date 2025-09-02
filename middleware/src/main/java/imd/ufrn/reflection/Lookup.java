package imd.ufrn.reflection;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import imd.ufrn.data.Headers;
import imd.ufrn.data.Request;
import imd.ufrn.utils.Paths;
import lombok.Getter;

public class Lookup {
  @Getter
  private static final Lookup instance = new Lookup();
  private List<LookupEntry> list;

  private Lookup() {
    this.list = new LinkedList<>();
  };

  public void register(
    LookupEntry entry
  ) {
    this.list.add(entry);
  };

  public Optional<Request<Object>> findRequest(LookupKey key) {
    return this.list.stream()
      .filter((request) -> request.key()
        .method()
        .equals(key.method())
      ).map((mapping) -> {
        // TODO - Mapear corretamente
        List<String> params = Paths.match(
          key.path(), 
          mapping.key().path()
        );

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
          request.setMethod(key.method());
          request.setPath(key.path());
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
