package az.caspian.nserv.http;

import java.nio.charset.StandardCharsets;

import static az.caspian.nserv.http.HttpHeaders.CONTENT_LENGTH;
import static az.caspian.nserv.http.HttpHeaders.CONTENT_TYPE;

public class HttpResponse {
  private final HttpStatus status;
  private final String contentType;
  private final String body;

  public HttpResponse(HttpStatus status, String contentType, String body) {
    this.status = status;
    this.contentType = contentType;
    this.body = body;
  }

  public String getBody() {
    return body;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public String getContentType() {
    return contentType;
  }

  public long getBodySize() {
    if (body != null) {
      return body.getBytes(StandardCharsets.UTF_8).length;
    }

    return 0;
  }

  public String getOutput() {
    return HttpConstants.HTTP_1_1 + " " +
        this.getStatus().statusWithCode() +
        HttpConstants.HTTP_LINE_SEPARATOR +
        new HttpHeader(CONTENT_TYPE, this.getContentType()) +
        HttpConstants.HTTP_LINE_SEPARATOR +
        new HttpHeader(CONTENT_LENGTH, this.getBodySize()) +
        HttpConstants.HTTP_LINE_SEPARATOR +
        HttpConstants.HTTP_LINE_SEPARATOR +
        this.getBody() +
        HttpConstants.HTTP_LINE_SEPARATOR;
  }
}
