package az.caspian.nserv;

import az.caspian.nserv.http.*;

import java.io.IOException;
import java.io.OutputStream;

public class HttpResponseHandler {

  private static final String HELLO_WORLD_MSG = "Hello World!";

  public void handle(OutputStream outputStream) throws IOException {
    StringBuilder builder = new StringBuilder();
    builder
        .append(HttpConstants.HTTP_1_1).append(" ")
        .append(HttpStatus.OK.statusWithCode())
        .append(System.lineSeparator())
        .append(new HttpHeader(HttpHeaders.CONTENT_TYPE, ContentTypes.TEXT_PLAIN_UTF_8))
        .append(System.lineSeparator())
        .append(new HttpHeader(HttpHeaders.CONTENT_LENGTH, HELLO_WORLD_MSG.length()))
        .append(System.lineSeparator())
        .append(System.lineSeparator())
        .append(HELLO_WORLD_MSG)
        .append(System.lineSeparator());
    outputStream.write(builder.toString().getBytes(), 0, builder.length());
    outputStream.flush();
    outputStream.close();
  }

}
