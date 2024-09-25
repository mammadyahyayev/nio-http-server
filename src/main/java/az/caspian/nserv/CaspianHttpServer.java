package az.caspian.nserv;

import az.caspian.nserv.connection.AccessLogWriter;
import az.caspian.nserv.connection.ConnectionHandler;
import az.caspian.nserv.http.HttpConstants;
import az.caspian.nserv.http.HttpRequestHandler;
import az.caspian.nserv.http.HttpResponseHandler;
import az.caspian.nserv.router.RouterDefiner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.ServiceLoader;

public class CaspianHttpServer {
  private static final Logger log = LogManager.getLogger();

  private final int port;
  private final String host;
  private static final String HTTP_VERSION = HttpConstants.HTTP_1_1;

  static {
    System.setProperty("server.http.version", HTTP_VERSION);
  }

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

    var loader = ServiceLoader.load(RouterDefiner.class);
    RouterDefiner routerDefiner = loader.findFirst().orElseThrow();

    var connectionHandler = new ConnectionHandler(
      new HttpRequestHandler(),
      new HttpResponseHandler(routerDefiner),
      new AccessLogWriter()
    );

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
