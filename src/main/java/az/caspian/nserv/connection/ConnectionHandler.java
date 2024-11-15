package az.caspian.nserv.connection;

import az.caspian.nserv.HttpServerConfig;
import az.caspian.nserv.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionHandler {
  private static final Logger log = LogManager.getLogger();

  private static final AtomicInteger availableThreadCount = new AtomicInteger(0);
  private static final ConcurrentLinkedQueue<Socket> connectionSockets = new ConcurrentLinkedQueue<>();

  private final Lock lock = new ReentrantLock();
  private final Condition notEmpty = lock. newCondition();

  private final HttpRequestHandler httpRequestHandler;
  private final HttpResponseHandler httpResponseHandler;
  private final HttpRequestListener requestListener;

  {
    while (HttpServerConfig.MAX_THREAD_COUNT >= availableThreadCount.get()) {
      var thread = new Thread(() -> {
        lock.lock();
        String currentThreadName = Thread.currentThread().getName();
        log.debug("{} is created and {} connections are waiting", currentThreadName, connectionSockets.size());
        try {
          notEmpty.await();
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        Socket socket = connectionSockets.poll();
        if (socket != null) {
          log.debug("{} is polling connection socket to handle", currentThreadName);
          handleConnection(socket);
        }
      });
      thread.start();
      availableThreadCount.getAndIncrement();
    }
  }

  public ConnectionHandler(
    HttpRequestHandler httpRequestHandler,
    HttpResponseHandler httpResponseHandler,
    HttpRequestListener requestListener
  ) {
    this.httpRequestHandler = httpRequestHandler;
    this.httpResponseHandler = httpResponseHandler;
    this.requestListener = requestListener;
  }

  public void handleConnections(ServerSocket serverSocket) throws IOException {
    while (true) {
      Socket connectionSocket = serverSocket.accept();
      connectionSockets.add(connectionSocket);
      notEmpty.signalAll();
    }
  }

  private void handleConnection(Socket socket) {
    try {
      HttpRequest request = httpRequestHandler.handle(socket.getInetAddress().getHostAddress(), socket.getInputStream());
      HttpResponse response = httpResponseHandler.handle(request);
      requestListener.onRequest(request, response);
      sendResponse(response, socket.getOutputStream());
    } catch (IOException e) {
      log.error("Error while handling connection: {}", e.getMessage());
    }
  }

  private void sendResponse(HttpResponse httpResponse, OutputStream outputStream) throws IOException {
    String output = httpResponse.getOutput();
    outputStream.write(output.getBytes(), 0, output.length());
    outputStream.flush();
    outputStream.close();
  }
}

