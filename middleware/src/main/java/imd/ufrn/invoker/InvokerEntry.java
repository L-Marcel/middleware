package imd.ufrn.invoker;

import java.lang.reflect.Method;
import java.util.List;

public record InvokerEntry(
  List<InvokerEntryParam> params,
  Method remote
) {};
