package imd.ufrn.lookup;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import imd.ufrn.invoker.InvokerEntry;
import imd.ufrn.invoker.InvokerEntryParam;

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

  public Optional<InvokerEntry> findInvokerEntry(LookupKey key) {
    return this.list.stream()
      .filter((entry) -> entry.key()
        .method()
        .equals(key.method())
      ).map((entry) -> {
        List<InvokerEntryParam> params = this.match(
          entry.params(),
          key.path(), 
          entry.key().path()
        );

        if(params == null) 
          return null;
        
        return new InvokerEntry(
          entry.before(),
          entry.after(),
          params,
          entry.controller(),
          entry.remote()
        );
      }).filter((_class) -> _class != null)
      .findFirst();
  };

  public List<InvokerEntryParam> match(
    List<LookupEntryParam> params,
    String path, 
    String expected
  ) {
    Map<String, String> values = new LinkedHashMap<>();
    
    String[] paths = path.split("/");
    String[] expecteds = expected.split("/");

    if(paths.length != expecteds.length) {
      return null;
    };

    for(int i = 0; i < paths.length; i++) {
      String value = paths[i].trim();
      String id = expecteds[i].trim();

      if(id.startsWith("{") & id.endsWith("}")) {
        id = id.replace("{", "").replace("}", "");
        values.put(id, value);
      } else if(!id.equals(value)) {
        return null;
      };
    };

    return params.stream()
      .map((entry) -> new InvokerEntryParam(
        entry.id(),
        entry.body(),
        entry.required(),
        entry.type(),
        values.getOrDefault(entry.id(), null)
      )).toList();
  };
};


