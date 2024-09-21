package az.caspian.nserv.http;

public class HttpHeader {
  private final String header;
  private final Object value;

  public HttpHeader(String header, Object value) {
    this.header = header;
    this.value = value;
  }

  @Override
  public String toString() {
    return header + ": " + value;
  }
}
