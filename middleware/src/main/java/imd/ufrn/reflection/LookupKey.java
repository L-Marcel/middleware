package imd.ufrn.reflection;

import imd.ufrn.enums.HttpMethod;

public record LookupKey(
  HttpMethod method, 
  String path
) {};
