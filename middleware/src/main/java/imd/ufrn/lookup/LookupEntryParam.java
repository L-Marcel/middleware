package imd.ufrn.lookup;

public record LookupEntryParam(
  String id,
  boolean body,
  Class<?> type,
  Object instance
) {};