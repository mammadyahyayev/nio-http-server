package az.caspian.nserv.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

public class HttpRequestHandler {
  private static final Logger log = LogManager.getLogger();

  private final HttpRequestListener httpRequestListener;

  public HttpRequestHandler(HttpRequestListener httpRequestListener) {
    this.httpRequestListener = httpRequestListener;
  }

  public void handle(String ipAddress, InputStream inputStream) throws IOException {
    var request = new StringBuilder();
    var buffer = new byte[1024];

    while (inputStream.available() != 0) {
      int read = inputStream.read(buffer);
      log.debug("bytes read: {}", read);
      log.debug("available buffer size: {}", buffer.length - read);
      request.append(new String(buffer, 0, read));
    }

    log.debug("http request: \n{}", request);
    HttpRequest httpRequest = buildHttpRequest(ipAddress, request.toString());
    httpRequestListener.onRequest(httpRequest);
  }

  private HttpRequest buildHttpRequest(String ipAddress, String requestMessage) {
    String[] requestLines = requestMessage.split("\r\n");

    String[] requestLine = requestLines[0].split(" ");

    String method = requestLine[0];
    String path = requestLine[1];
    String httpVersion = requestLine[2];

    if (!HttpRequestValidator.isSupportedHttpVersion(httpVersion)) {
      String errorMsg = MessageFormat.format(
          "HTTP version {0} not supported, HTTP server supports {1} version and below",
          httpVersion,
          System.getProperty("server.http.version")
      );
      throw new IllegalArgumentException(errorMsg);
    }

    if (!HttpRequestValidator.isValidHttpMethod(method)) {
      throw new IllegalArgumentException("Invalid HTTP method: " + method);
    }

    log.debug("Method: {}\nPath: {}", method, path);
    return new HttpRequest(ipAddress, method, path);
  }
}
