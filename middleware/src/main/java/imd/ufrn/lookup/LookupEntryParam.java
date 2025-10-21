package imd.ufrn.lookup;

public record LookupEntryParam(
  String id,
  boolean body,
  boolean required,
  Class<?> type
) {};