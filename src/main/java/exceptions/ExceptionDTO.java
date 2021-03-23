package exceptions;

public class ExceptionDTO {

  private int code;
  private String message;

  public ExceptionDTO() {
  }

  public ExceptionDTO(int code, String description){
    this.code = code;
    this.message = description;
  }

  public ExceptionDTO(String message) {
    this.message = message;
  }

  public ExceptionDTO(Exception exception) {
    this.message = exception.getMessage();
  }

  public ExceptionDTO(Throwable cause) {
    this.message = cause.getMessage();
  }
}