package imd.ufrn.lookup;

import java.lang.reflect.Method;
import java.util.List;

import imd.ufrn.interceptors.AfterInterceptor;
import imd.ufrn.interceptors.BeforeInterceptor;

public record LookupEntry(
  LookupKey key,
  List<BeforeInterceptor> before,
  List<AfterInterceptor> after,
  List<LookupEntryParam> params,
  Object instance,
  Method remote
) {};
