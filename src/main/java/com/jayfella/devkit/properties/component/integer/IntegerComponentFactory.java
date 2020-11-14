package com.jayfella.devkit.properties.component.integer;


import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jayfella.devkit.properties.component.SDKComponentFactory;

public class IntegerComponentFactory implements SDKComponentFactory<Integer> {

  @Override
  public AbstractSDKComponent<Integer> create(Integer object ,String propertyName) {
    return new IntegerComponent(object, propertyName);
  }
}
