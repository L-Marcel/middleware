package imd.ufrn.reflection;

import imd.ufrn.data.Request;
import imd.ufrn.data.Response;
import imd.ufrn.enums.HttpMethod;

public record LookupEntry(
  HttpMethod method,
  String path,
  Class<Object> bodyClass,
  Class<Request<Object>> request,
  Class<Response<Object>> response
) {};
