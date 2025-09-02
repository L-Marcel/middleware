package imd.ufrn.reflection;

import imd.ufrn.data.Request;
import imd.ufrn.data.Response;

public record LookupEntry(
  LookupKey key,
  Class<Object> bodyClass,
  Class<Request<Object>> request,
  Class<Response<Object>> response
) {};
