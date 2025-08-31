package imd.ufrn.handler.listeners;

import java.util.Optional;

import imd.ufrn.data.Content;
import imd.ufrn.data.connection.Connection;

@FunctionalInterface
public interface ServerProcess {
  public Optional<Content> run(
    Content content,
    Optional<Connection> connection
  );
};
