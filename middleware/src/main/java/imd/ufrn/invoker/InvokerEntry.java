package imd.ufrn.invoker;

import java.lang.reflect.Method;
import java.util.List;

import imd.ufrn.interceptors.AfterInterceptor;
import imd.ufrn.interceptors.BeforeInterceptor;

public record InvokerEntry(
  List<BeforeInterceptor> before,
  List<AfterInterceptor> after,
  List<InvokerEntryParam> params,
  Object instance,
  Method remote
) {};
