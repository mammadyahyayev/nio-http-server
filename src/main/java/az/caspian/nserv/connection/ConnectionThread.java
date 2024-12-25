package az.caspian.nserv.connection;

import az.caspian.nserv.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

public class ConnectionThread extends Thread {
    private static final Logger log = LogManager.getLogger(ConnectionThread.class);

    private final Socket socket;
    private final HttpRequestHandler requestHandler;
    private final HttpResponseHandler responseHandler;
    private final HttpRequestListener requestListener;

    public ConnectionThread(Socket socket,
                            HttpRequestHandler requestHandler,
                            HttpResponseHandler responseHandler,
                            HttpRequestListener requestListener) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.responseHandler = responseHandler;
        this.requestListener = requestListener;
    }

    @Override
    public void run() {
        log.trace("Request is processing by {} at {}", this.getName(), LocalDateTime.now());
        try {
            HttpRequest request =
                    requestHandler.handle(socket.getInetAddress().getHostAddress(), socket.getInputStream());
            HttpResponse response = responseHandler.handle(request);
            requestListener.onRequest(request, response);
            sendResponse(response, socket.getOutputStream());
        } catch (IOException e) {
            log.error("Error while handling connection: {}", e.getMessage());
        }
        log.trace("Request is processed by {} at {}", this.getName(), LocalDateTime.now());
    }

    private void sendResponse(HttpResponse httpResponse, OutputStream outputStream) throws IOException {
        String output = httpResponse.getOutput();
        outputStream.write(output.getBytes(), 0, output.length());
        outputStream.flush();
        outputStream.close();
    }
}
