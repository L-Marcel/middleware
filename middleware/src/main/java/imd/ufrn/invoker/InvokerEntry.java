package imd.ufrn.invoker;

import java.lang.reflect.Method;
import java.util.List;

public record InvokerEntry(
  List<String> before,
  List<String> after,
  List<InvokerEntryParam> params,
  String controller,
  Method remote
) {};
