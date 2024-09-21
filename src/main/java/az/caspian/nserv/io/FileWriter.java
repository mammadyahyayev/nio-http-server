package az.caspian.nserv.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class FileWriter {
  private FileWriter() {
  }

  private static final Logger log = LogManager.getLogger();

  public static void writeLine(Path path, String line) {
    try {
      Files.writeString(path, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    } catch (IOException e) {
      log.error("Failed to write line to file", e);
    }
  }

}
