package imd.ufrn.lookup;

import java.lang.reflect.Method;
import java.util.List;

public record LookupEntry(
  LookupKey key,
  List<LookupEntryParam> params,
  Object instance,
  Method remote
) {};
