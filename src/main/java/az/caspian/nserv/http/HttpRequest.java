package az.caspian.nserv.http;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
  private final String ipAddress;
  private final String method;
  private final String path;
  private final Map<String, String> headers;
  private final String body;

  public HttpRequest(String ipAddress, String method, String path) {
    this(ipAddress, method, path, new HashMap<>(), null);
  }

  public HttpRequest(String ipAddress, String method, String path, Map<String, String> headers) {
    this(ipAddress, method, path, headers, null);
  }

  public HttpRequest(String ipAddress, String method, String path, Map<String, String> headers, String body) {
    this.ipAddress = ipAddress;
    this.method = method;
    this.path = path;
    this.headers = headers;
    this.body = body;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public String getMethod() {
    return method;
  }

  public String getPath() {
    return path;
  }

  public long getBodySize() {
    if (body != null) {
      return body.getBytes(StandardCharsets.UTF_8).length;
    }

    return 0;
  }

  public String getUserAgent() {
    if (headers.isEmpty()) {
      return "";
    }

    return headers.getOrDefault(HttpHeaders.USER_AGENT, "");
  }
}
