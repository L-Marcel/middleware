package imd.ufrn.invoker;

public record InvokerEntryParam(
  String id,
  boolean body,
  boolean required,
  Class<?> type,
  String value
) {};