package imd.ufrn.interceptors;

import imd.ufrn.invoker.InvokerEntry;
import imd.ufrn.lookup.LookupKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvocationContext {
  private LookupKey key;
  private InvokerEntry invoke;
  private Object[] params;
};
