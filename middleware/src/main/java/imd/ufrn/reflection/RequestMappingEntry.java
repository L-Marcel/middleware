package imd.ufrn.reflection;

import imd.ufrn.data.Request;
import imd.ufrn.data.Response;
import imd.ufrn.enums.Method;

public record RequestMappingEntry(
  Method method,
  String path,
  Class<Object> bodyClass,
  Class<Request<Object>> request,
  Class<Response<Object>> response
) {};
