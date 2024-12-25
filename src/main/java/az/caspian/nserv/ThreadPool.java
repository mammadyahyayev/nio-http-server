package az.caspian.nserv;

import java.util.ArrayList;
import java.util.List;

public class ThreadPool {

  private static final List<Thread> THREADS = new ArrayList<>();
  private static int availableThreadCount = 0;

  public ThreadPool() {
    init();
  }

  public void init() {
    while (HttpServerConfig.MAX_THREAD_COUNT >= availableThreadCount) {
      var thread = new Thread();
      THREADS.add(thread);
    }
  }
}
