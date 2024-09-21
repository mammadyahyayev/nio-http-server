package az.caspian.nserv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionHandler {
  private static final Logger log = LogManager.getLogger();
  private static int handledConnectionCount = 0;

  private static final String HELLO_WORLD_MSG = "Hello World!";

  public void handleConnections(ServerSocket serverSocket) throws IOException {
    while (true) {
      Socket connectionSocket = serverSocket.accept();
      handleConnection(connectionSocket);
      handledConnectionCount++;
    }
  }

  private void handleConnection(Socket socket) {
    try {
      OutputStream outputStream = socket.getOutputStream();
      StringBuilder builder = new StringBuilder();
      builder
          .append(HttpConstants.VERSION).append(" ")
          .append(HttpStatus.OK.statusWithCode())
          .append(System.lineSeparator())
          .append(new HttpHeader(HttpHeaders.CONTENT_TYPE, ContentTypes.TEXT_PLAIN_UTF_8))
          .append(System.lineSeparator())
          .append(new HttpHeader(HttpHeaders.CONTENT_LENGTH, HELLO_WORLD_MSG.length()))
          .append(System.lineSeparator())
          .append(System.lineSeparator())
          .append(HELLO_WORLD_MSG)
          .append(System.lineSeparator());
      outputStream.write(builder.toString().getBytes(), 0, builder.length());
      outputStream.flush();
      outputStream.close();
      log.debug("Connection #{} is handled and closed", handledConnectionCount);
    } catch (IOException e) {
      // socket is closed
    }
  }
}

