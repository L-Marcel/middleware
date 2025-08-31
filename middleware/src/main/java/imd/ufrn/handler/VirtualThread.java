package imd.ufrn.handler;

public abstract class VirtualThread {
  private Thread virtual;

  public VirtualThread() {
    this.virtual = Thread.ofVirtual()
      .unstarted(this::run);
  };

  public VirtualThread(
    String name
  ) {
    this.virtual = Thread.ofVirtual()
      .name(name)
      .unstarted(this::run);
  };

  public String getName() {
    return this.virtual.getName();
  };

  public void start() {
    this.virtual.start();
  };

  public void join() throws InterruptedException {
    this.virtual.join();
  };

  public abstract void run();
};
