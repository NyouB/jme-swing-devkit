package com.jayfella.devkit.properties.component.string;


import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jayfella.devkit.properties.component.SDKComponentFactory;

public class StringComponentFactory implements SDKComponentFactory<String> {

  @Override
  public AbstractSDKComponent<String> create(String object ,String propertyName) {
    return new StringComponent(object ,propertyName);
  }
}
