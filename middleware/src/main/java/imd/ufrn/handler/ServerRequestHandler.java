package imd.ufrn.handler;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import imd.ufrn.DawnSettings;
import imd.ufrn.data.Content;
import imd.ufrn.data.Request;
import imd.ufrn.data.Response;
import imd.ufrn.data.connection.Connection;
import imd.ufrn.handler.listeners.ServerListener;
import imd.ufrn.handler.listeners.TCPListener;
import imd.ufrn.handler.listeners.UDPListener;
import imd.ufrn.handler.listeners.implementations.ConcreteApplicationImplementation;
import lombok.Getter;

@Getter
public class ServerRequestHandler extends VirtualThread {
  protected ServerListener listener;
  protected ExecutorService executor;

  public ServerRequestHandler(DawnSettings settings) {
    switch (settings.transport()) {
      case UDP:
        this.listener = new UDPListener();
        break;
      case TCP:
      default:
        this.listener = new TCPListener();
        break;
    };

    this.listener.setApplication(
      new ConcreteApplicationImplementation()
    );

    this.listener.setProcess(this::process);

    this.startup(
      settings.port(), 
      settings.backlog(), 
      settings.timeout()
    );

    if(settings.multithread())
      this.executor = Executors.newVirtualThreadPerTaskExecutor();
  };
  
  public Optional<Content> process(
    Content content,
    Optional<Connection> connection
  ) {
    if(content instanceof Request) {
      // TODO - fazer algo com o request
      return Optional.empty();
    };

    return Optional.empty();
  };

  private void startup(
    int port,
    int backlog, 
    Duration timeout
  ) {
    try {
      listener.bind(
        port,
        backlog,
        timeout
      );
    
      System.out.println(
        "[Server] Servidor " + 
        this.listener.getTransportProtocol() + 
        " escutando na porta " + port
      );
    } catch (Exception e) {
      e.printStackTrace();
    };
  };

  @Override
  public void run() {
    while (true) {
      try {
        Runnable task = this.listener.accept();
        if(this.executor == null) task.run();
        else this.executor.submit(task);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  };
};
