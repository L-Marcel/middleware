package imd.ufrn.invoker;

public record InvokerEntryParam(
  String id,
  boolean body,
  Class<?> type,
  Object instance,
  String value
) {};