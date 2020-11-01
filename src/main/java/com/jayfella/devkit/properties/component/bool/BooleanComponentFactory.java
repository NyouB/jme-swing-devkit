package com.jayfella.devkit.properties.component.bool;

import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jayfella.devkit.properties.component.SDKComponentFactory;

public class BooleanComponentFactory implements SDKComponentFactory<Boolean> {

  @Override
  public AbstractSDKComponent<Boolean> create(Boolean object) {
    return new BooleanComponent(object);
  }
}
