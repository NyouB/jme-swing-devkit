package com.jayfella.devkit.properties.component.enumeration;


import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jayfella.devkit.properties.component.SDKComponentFactory;

public class EnumComponentFactory implements SDKComponentFactory<Enum> {

  @Override
  public AbstractSDKComponent<Enum> create(Enum object, String propertyName) {
    return new EnumComponent(object ,propertyName);
  }
}
