package imd.ufrn.invoker;

public record InvokerEntryParam(
  String id,
  boolean body,
  Class<Object> type,
  String value
) {};