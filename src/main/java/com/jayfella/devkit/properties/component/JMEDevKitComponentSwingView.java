package com.jayfella.devkit.properties.component;

import javax.swing.JPanel;

public abstract class JMEDevKitComponentSwingView<T> extends JPanel {

  protected T component;
  protected String propertyName = "";

  public JMEDevKitComponentSwingView(T component) {
    this.component = component;
    this.propertyName = toString();
  }

  public JMEDevKitComponentSwingView(T component, String propertyName) {
    this.component = component;
    this.propertyName = propertyName;
  }

  public void setPropertyName(String propertyName){
    this.propertyName = propertyName;
  }

  public void setComponent(T component){
    this.component = component;
  }

  public T getComponent() {
    return component;
  }
}
