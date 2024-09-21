package az.caspian.nserv.http;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
  private final String method;
  private final String path;
  private final Map<String, String> headers;
  private final String body;

  public HttpRequest(String method, String path) {
    this.method = method;
    this.path = path;
    this.headers = new HashMap<>();
    this.body = null;
  }

  public HttpRequest(String method, String path, Map<String, String> headers, String body) {
    this.method = method;
    this.path = path;
    this.headers = headers;
    this.body = body;
  }
}
