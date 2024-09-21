package az.caspian.nserv.http;

public final class HttpRequestValidator {
  private HttpRequestValidator() {}

  public static boolean isSupportedHttpVersion(String httpVersion) {
    return httpVersion.equals(System.getProperty("server.http.version"));
  }

  public static boolean isValidHttpMethod(String httpMethod) {
    return switch (httpMethod) {
      case "GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS" -> true;
      default -> false;
    };
  }
}
