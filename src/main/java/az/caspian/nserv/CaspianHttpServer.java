package az.caspian.nserv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class CaspianHttpServer {
  private static final Logger log = LogManager.getLogger();

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
    log.info("Starting server on port {}", port);
    var connectionHandler = new ConnectionHandler(new HttpRequestHandler(), new HttpResponseHandler());

    try (var serverSocket = new ServerSocket()) {
      serverSocket.setReuseAddress(true);
      serverSocket.bind(new InetSocketAddress(host, port));
      log.info("Server started on port {}", port);
      connectionHandler.handleConnections(serverSocket);
    } catch (IOException e) {
      log.error("Failed to start server: {}", e.getMessage());
    }
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
