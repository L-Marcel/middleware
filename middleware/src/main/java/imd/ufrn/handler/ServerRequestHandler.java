package imd.ufrn.handler;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import imd.ufrn.DawnSettings;
import imd.ufrn.handler.strategies.RequestHandler;
import imd.ufrn.handler.strategies.TCPRequestHandler;
import imd.ufrn.handler.strategies.UDPRequestHandler;
import lombok.Getter;

@Getter
public class ServerRequestHandler extends VirtualThread {
  protected RequestHandler handler;
  protected ExecutorService executor;
  
  public ServerRequestHandler(DawnSettings settings) {
    switch (settings.transport()) {
      case UDP:
        this.handler = new UDPRequestHandler();
        break;
      case TCP:
      default:
        this.handler = new TCPRequestHandler();
        break;
    };

    this.startup(
      settings.port(), 
      settings.backlog(), 
      settings.timeout()
    );

    if(settings.multithread())
      this.executor = Executors.newVirtualThreadPerTaskExecutor();
  };
  
  private void startup(
    int port,
    int backlog, 
    Duration timeout
  ) {
    try {
      this.handler.bind(
        port,
        backlog,
        timeout
      );
    
      System.out.println(
        "[Server] Servidor " + 
        this.handler.getTransportProtocol() + 
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
        Runnable task = this.handler.accept();
        if(this.executor == null) task.run();
        else this.executor.submit(task);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  };
};
