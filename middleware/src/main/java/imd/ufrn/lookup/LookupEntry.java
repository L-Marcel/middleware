package imd.ufrn.lookup;

import java.lang.reflect.Method;
import java.util.List;

import imd.ufrn.interceptors.Interceptor;

public record LookupEntry(
  LookupKey key,
  List<Interceptor> before,
  List<Interceptor> after,
  List<LookupEntryParam> params,
  Object instance,
  Method remote
) {};
