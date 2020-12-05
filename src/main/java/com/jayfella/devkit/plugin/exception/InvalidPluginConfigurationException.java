package com.jayfella.devkit.plugin.exception;

public class InvalidPluginConfigurationException extends Exception {

  private static final long serialVersionUID = -3791024724953336272L;

  public InvalidPluginConfigurationException() {
  }

  public InvalidPluginConfigurationException(String message) {
    super(message);
  }

  public InvalidPluginConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidPluginConfigurationException(Throwable cause) {
    super(cause);
  }

  public InvalidPluginConfigurationException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
