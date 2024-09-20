package az.caspian.nserv;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConnectionHandler {
  private static final Log log = LogFactory.getLog(ConnectionHandler.class);

  private static final String HELLO_WORLD_MSG = "Hello World!";

  private static final List<Socket> SOCKETS = new ArrayList<>();
  private boolean shouldStop = false;

  public ConnectionHandler() {

  }

  public void addSocket(Socket socket) {
    Objects.requireNonNull(socket);
    SOCKETS.add(socket);
  }

  public void handleSockets() {
    log.debug("Handling incoming connections...");
    while (!shouldStop) {
      for (Socket socket : SOCKETS) {
        handleSocket(socket);
      }
    }
  }

  private void handleSocket(Socket socket) {
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
    } catch (IOException e) {
      // socket is closed
    }
  }
}

