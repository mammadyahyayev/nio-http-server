package az.caspian.nserv.connection;

import az.caspian.nserv.http.HttpRequest;
import az.caspian.nserv.http.HttpRequestListener;
import az.caspian.nserv.io.FileWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class AccessLogWriter implements HttpRequestListener {

  private static final Logger log = LogManager.getLogger(AccessLogWriter.class);

  private static final String ATTRIBUTE_SEPARATOR = " -- ";

  private final Path accessLogFilePath;

  public AccessLogWriter() {
    this.accessLogFilePath = Path.of(System.getProperty("user.home"), ".nserv-access.log");
  }

  public AccessLogWriter(Path accessLogFilePath) {
    this.accessLogFilePath = accessLogFilePath;
  }

  @Override
  public void onRequest(HttpRequest request) {
    log.debug("Listening on request to write logs to access.log file on path {}", accessLogFilePath);

    String requestLine = request.getMethod() + " " + request.getPath() + " " + System.getProperty("server.http.version");
    LocalDateTime dateTime = LocalDateTime.now(ZoneOffset.UTC);
    String logLine = constructLogLine(request.getIpAddress(), dateTime, requestLine, request.getUserAgent());
    FileWriter.writeLine(accessLogFilePath, logLine);
  }

  private String constructLogLine(Object... args) {
    StringBuilder logLine = new StringBuilder();
    for (int i = 0; i < args.length; i++) {
      logLine.append(args[i]);
      if (i < args.length - 1) {
        logLine.append(ATTRIBUTE_SEPARATOR);
      } else {
        logLine.append(System.lineSeparator());
      }
    }

    return logLine.toString();
  }

}
