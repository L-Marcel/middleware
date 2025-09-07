package imd.ufrn.errors;

import imd.ufrn.data.StatusCode;

public record ErrorPayload(
  StatusCode code,
  String message
) {};
