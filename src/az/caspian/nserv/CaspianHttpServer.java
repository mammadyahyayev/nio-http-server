package az.caspian.nserv;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class CaspianHttpServer {
  private static final Logger LOG = Logger.getLogger(CaspianHttpServer.class.getName());

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
    LOG.info("Starting server on port " + port);
    var connectionHandler = new ConnectionHandler();

    try (var serverSocket = new ServerSocket()) {
      serverSocket.setReuseAddress(true);
      serverSocket.bind(new InetSocketAddress(host, port));
      LOG.info("Server started on port " + port);
      Socket connectionSocket = serverSocket.accept();
      connectionHandler.addSocket(connectionSocket);
    } catch (IOException e) {
      LOG.severe("Failed to start server: " + e.getMessage());
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
