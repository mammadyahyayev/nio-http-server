package az.caspian.nserv.http;

import az.caspian.nserv.io.HtmlFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

public class HttpResponseHandler {
  private static final Logger log = LogManager.getLogger(HttpResponseHandler.class);

  public HttpResponse handle(HttpRequest httpRequest) {
    String htmlFileName;
    if (httpRequest == null) {
      htmlFileName = "404.html";
    } else {
      final String requestPath = httpRequest.getPath();
      htmlFileName = switch (requestPath) {
        case "/" -> "index.html";
        case "/students" -> "students.html";
        default -> "404.html";
      };
    }

    String content = HtmlFileReader.readFile(Path.of(HttpConstants.DEFAULT_STATIC_CONTENT_PATH, htmlFileName));
    return new HttpResponse(HttpStatus.OK, ContentTypes.TEXT_HTML, content);
  }
}
