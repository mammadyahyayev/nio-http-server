package az.caspian.nserv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

public class HttpRequestHandler {
  private static final Logger log = LogManager.getLogger();

  public void handle(InputStream inputStream) throws IOException {
    var request = new StringBuilder();
    var buffer = new byte[1024];

    while (inputStream.available() != 0) {
      int read = inputStream.read(buffer);
      log.debug("bytes read: {}", read);
      log.debug("available buffer size: {}", buffer.length - read);
      request.append(new String(buffer, 0, read));
    }

    log.debug("http request: \n{}", request);
  }

}
