package com.jayfella.devkit.properties.component.floatc;


import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jayfella.devkit.properties.component.SDKComponentFactory;

public class FloatComponentFactory implements SDKComponentFactory<Float> {

  @Override
  public AbstractSDKComponent<Float> create(Float object, String propertyName) {
    return new FloatComponent(object, propertyName);
  }
}
