package az.caspian.nserv.http;

import az.caspian.nserv.io.HtmlFileReader;
import az.caspian.nserv.router.Router;
import az.caspian.nserv.router.RouterDefiner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HttpResponseHandler {
  private static final Logger log = LogManager.getLogger(HttpResponseHandler.class);

  private final RouterDefiner routerDefiner;

  public HttpResponseHandler(RouterDefiner routerDefiner) {
    this.routerDefiner = routerDefiner;
  }

  public HttpResponse handle(HttpRequest httpRequest) {
    String htmlFileName;
    if (httpRequest == null) {
      htmlFileName = "404.html";
    } else {
      List<Router> routers = routerDefiner.getRouters();
      final String requestPath = httpRequest.getPath();
      htmlFileName = findFilenameByRouter(requestPath, routers);
    }

    String content = HtmlFileReader.readFile(Path.of(HttpConstants.DEFAULT_STATIC_CONTENT_PATH, htmlFileName));
    return new HttpResponse(HttpStatus.OK, ContentTypes.TEXT_HTML, content);
  }

  private String findFilenameByRouter(String requestPath, List<Router> routers) {
    if (requestPath.equals("/students")) {
      try {
        Thread.sleep(TimeUnit.SECONDS.toMillis(30));
        log.info("Thread {} sleep is over", Thread.currentThread().getName());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    for (Router router : routers) {
      if (router.path().equals(requestPath)) {
        return router.response();
      }
    }

    return "404.html";
  }
}
