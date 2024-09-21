package az.caspian.nserv.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

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
    String[] requestLines = requestMessage.split(HttpConstants.HTTP_LINE_SEPARATOR);

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

    Map<String, String> httpRequestHeaders = buildHttpRequestHeaders(requestLines);

    return new HttpRequest(ipAddress, method, path, httpRequestHeaders);
  }

  private Map<String, String> buildHttpRequestHeaders(String[] requestLines) {
    Map<String, String> requestHeaders = new HashMap<>();
    for (int i = 1; i < requestLines.length; i++) {
      if (!requestLines[i].equals(HttpConstants.HTTP_LINE_SEPARATOR)) {
        String[] headerSplit = requestLines[i].split(HttpConstants.HTTP_HEADER_SEPARATOR);
        String header = headerSplit[0].trim();
        String value = headerSplit[1];
        requestHeaders.put(header, value);
      }
    }

    return requestHeaders;
  }
}
