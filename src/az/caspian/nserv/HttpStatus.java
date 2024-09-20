package az.caspian.nserv;

public enum HttpStatus {
  OK(200),
  CREATED(201),
  ACCEPTED(202),
  NOT_FOUND(404);

  private final int code;

  HttpStatus(int code) {
    this.code = code;
  }

  public int code() {
    return code;
  }

  public String statusWithCode() {
    return this.code + " " + this.name();
  }
}
