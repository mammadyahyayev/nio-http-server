package az.caspian.nserv.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.lang.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class HtmlFileReader {
  private HtmlFileReader() {
  }

  private static final Logger log = LogManager.getLogger(HtmlFileReader.class);

  public static @Nullable String readFile(Path path) {
    try {
      Path filePath = Path.of("src/main/resources").resolve(path);
      List<String> lines = Files.readAllLines(filePath);
      return String.join("\n", lines);
    } catch (IOException e) {
      log.error("Failed to read file", e);
      return null;
    }
  }
}
