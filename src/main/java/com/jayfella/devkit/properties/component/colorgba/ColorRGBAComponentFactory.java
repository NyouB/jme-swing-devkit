package com.jayfella.devkit.properties.component.colorgba;

import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jayfella.devkit.properties.component.SDKComponentFactory;
import com.jme3.math.ColorRGBA;

public class ColorRGBAComponentFactory implements SDKComponentFactory<ColorRGBA> {

  @Override
  public AbstractSDKComponent<ColorRGBA> create(ColorRGBA object, String propertyName) {
    return new ColorRGBAComponent(object, propertyName);
  }
}
