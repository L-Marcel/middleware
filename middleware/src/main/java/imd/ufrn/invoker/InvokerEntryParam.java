package imd.ufrn.invoker;

public record InvokerEntryParam(
  String id,
  Class<? extends Object> type,
  String value
) {};