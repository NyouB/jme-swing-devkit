package fr.exratio.jme.devkit.event;

public class EventThreadingException extends Exception {

  private static final long serialVersionUID = 4085485140413458792L;

  public EventThreadingException() {
  }

  public EventThreadingException(String message) {
    super(message);
  }

  public EventThreadingException(String message, Throwable cause) {
    super(message, cause);
  }

  public EventThreadingException(Throwable cause) {
    super(cause);
  }

  public EventThreadingException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
