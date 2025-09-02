package imd.ufrn.lookup;

import imd.ufrn.enums.HttpMethod;

public record LookupKey(
  HttpMethod method, 
  String path
) {};
