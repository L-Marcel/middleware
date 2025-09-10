package imd.ufrn.invoker;

import java.lang.reflect.Method;
import java.util.List;

import imd.ufrn.interceptors.Interceptor;

public record InvokerEntry(
  List<Interceptor> before,
  List<Interceptor> after,
  List<InvokerEntryParam> params,
  Object instance,
  Method remote
) {};
