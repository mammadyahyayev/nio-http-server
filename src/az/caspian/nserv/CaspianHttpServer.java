package az.caspian.nserv;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class CaspianHttpServer {
  private static final Log log = LogFactory.getLog(CaspianHttpServer.class);

  private final int port;
  private final String host;

  public CaspianHttpServer() {
    this(8080, "127.0.0.1");
  }

  public CaspianHttpServer(int port) {
    this(port, "127.0.0.1");
  }

  public CaspianHttpServer(int port, String host) {
    this.port = port;
    this.host = host;
  }

  public void start() {
    log.info("Starting server on port " + port);
    var connectionHandler = new ConnectionHandler();

    try (var serverSocket = new ServerSocket()) {
      serverSocket.setReuseAddress(true);
      serverSocket.bind(new InetSocketAddress(host, port));
      log.info("Server started on port " + port);
      Socket connectionSocket = serverSocket.accept();
      connectionHandler.addSocket(connectionSocket);
    } catch (IOException e) {
      log.error("Failed to start server: " + e.getMessage());
    }

    connectionHandler.handleSockets();
  }

  public void stop() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  public int getPort() {
    return port;
  }

  public String getHost() {
    return host;
  }
}
