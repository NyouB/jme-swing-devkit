package com.jayfella.devkit.properties;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;

public class PropertySection {

  private final String title;
  private final Icon icon;
  private final Map<String, Component> components;

  public PropertySection(String title) {
    this(title, null, new HashMap<>());
  }

  public PropertySection(String title, Icon icon) {
    this(title, icon, new HashMap<>());
  }

  public PropertySection(String title, Map<String, Component> components) {
    this(title, null, components);
  }

  public PropertySection(String title, Icon icon, Map<String, Component> components) {
    this.title = title;
    this.icon = icon;
    this.components = components;
  }

  public Map<String, Component> getComponents() {
    return components;
  }

  public String getTitle() {
    return title;
  }

  public Icon getIcon() {
    return icon;
  }

  public void addProperty(String propertyName, Component component) {
    components.put(propertyName, component);
  }

}
