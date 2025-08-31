package imd.ufrn;

import java.time.Duration;

import imd.ufrn.enums.TransportProtocol;

public record DawnSettings(
  TransportProtocol transport,
  int port,
  Duration timeout,
  boolean multithread,
  int backlog
) {};