package imd.ufrn.lookup;

import java.lang.reflect.Method;
import java.util.List;

public record LookupEntry(
  LookupKey key,
  LookupEntryBody body,
  List<LookupEntryParam> params,
  Method remote
) {};
