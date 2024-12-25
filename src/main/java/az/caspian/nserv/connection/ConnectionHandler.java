package az.caspian.nserv.connection;

import az.caspian.nserv.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionHandler {
    private static final Logger log = LogManager.getLogger();

    private final HttpRequestHandler requestHandler;
    private final HttpResponseHandler responseHandler;
    private final HttpRequestListener requestListener;

    public ConnectionHandler(
            HttpRequestHandler requestHandler,
            HttpResponseHandler responseHandler,
            HttpRequestListener requestListener
    ) {
        this.requestHandler = requestHandler;
        this.responseHandler = responseHandler;
        this.requestListener = requestListener;
    }

    public void handleConnections(ServerSocket serverSocket) throws IOException {
        while (true) {
            Socket connectionSocket = serverSocket.accept();
            log.trace("New request send from {}", connectionSocket.getInetAddress().getHostAddress());
            new ConnectionThread(connectionSocket, requestHandler, responseHandler, requestListener).start();
        }
    }
}

