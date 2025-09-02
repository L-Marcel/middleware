package imd.ufrn.lookup;

public record LookupEntryParam(
  String id,
  Class<? extends Object> type
) {};