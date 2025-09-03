package imd.ufrn.lookup;

public record LookupEntryParam(
  String id,
  boolean body,
  Class<Object> type
) {};