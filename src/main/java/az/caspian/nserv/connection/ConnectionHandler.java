package az.caspian.nserv.connection;

import az.caspian.nserv.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionHandler {
  private static final Logger log = LogManager.getLogger();
  private static int handledConnectionCount = 0;

  private final HttpRequestHandler httpRequestHandler;
  private final HttpResponseHandler httpResponseHandler;

  public ConnectionHandler(HttpRequestHandler httpRequestHandler, HttpResponseHandler httpResponseHandler) {
    this.httpRequestHandler = httpRequestHandler;
    this.httpResponseHandler = httpResponseHandler;
  }

  public void handleConnections(ServerSocket serverSocket) throws IOException {
    while (true) {
      Socket connectionSocket = serverSocket.accept();
      handleConnection(connectionSocket);
      handledConnectionCount++;
    }
  }

  private void handleConnection(Socket socket) {
    try {
      httpRequestHandler.handle(socket.getInetAddress().getHostAddress(), socket.getInputStream());
      httpResponseHandler.handle(socket.getOutputStream());
      log.debug("Connection #{} is handled and closed", handledConnectionCount);
    } catch (IOException e) {
      log.error("Error while handling connection: {}", e.getMessage());
    }
  }
}

