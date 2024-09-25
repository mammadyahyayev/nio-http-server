package az.caspian.nserv.connection;

import az.caspian.nserv.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionHandler {
  private static final Logger log = LogManager.getLogger();
  private static int handledConnectionCount = 0;

  private final HttpRequestHandler httpRequestHandler;
  private final HttpResponseHandler httpResponseHandler;
  private final HttpRequestListener requestListener;

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
      handleConnection(connectionSocket);
      handledConnectionCount++;
    }
  }

  private void handleConnection(Socket socket) {
    try {
      HttpRequest request = httpRequestHandler.handle(socket.getInetAddress().getHostAddress(), socket.getInputStream());
      HttpResponse response = httpResponseHandler.handle(request);
      requestListener.onRequest(request, response);
      sendResponse(response, socket.getOutputStream());
      log.trace("Connection #{} is handled and closed", handledConnectionCount);
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

