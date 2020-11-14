package com.jayfella.devkit.properties.component;

public interface SDKComponentFactory<T> {

  AbstractSDKComponent<T> create(T object, String componentName);
}
