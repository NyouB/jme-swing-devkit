package fr.exratio.devkit.plugin.exception;

public class PluginDependencyNotFoundException extends Exception {

  private static final long serialVersionUID = 314168362230971177L;

  public PluginDependencyNotFoundException() {
  }

  public PluginDependencyNotFoundException(String message) {
    super(message);
  }

  public PluginDependencyNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public PluginDependencyNotFoundException(Throwable cause) {
    super(cause);
  }

  public PluginDependencyNotFoundException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
