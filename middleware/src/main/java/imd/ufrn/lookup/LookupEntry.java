package imd.ufrn.lookup;

import java.lang.reflect.Method;
import java.util.List;

public record LookupEntry(
  LookupKey key,
  List<String> before,
  List<String> after,
  List<LookupEntryParam> params,
  String controller,
  Method remote
) {};
