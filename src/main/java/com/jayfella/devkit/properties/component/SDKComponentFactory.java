package com.jayfella.devkit.properties.component;

public interface SDKComponentFactory<T> {

  AbstractPropertyEditor<T> create(T object, String componentName);
}
