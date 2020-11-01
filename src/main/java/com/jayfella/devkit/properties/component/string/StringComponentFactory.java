package com.jayfella.devkit.properties.component.string;


import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jayfella.devkit.properties.component.SDKComponentFactory;

public class StringComponentFactory implements SDKComponentFactory<String> {

  @Override
  public AbstractSDKComponent<String> create(String object) {
    return new StringComponent(object);
  }
}
