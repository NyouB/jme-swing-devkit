package com.jayfella.devkit.properties.component.vector2f;


import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jayfella.devkit.properties.component.SDKComponentFactory;
import com.jme3.math.Vector2f;

public class Vector2fComponentFactory implements SDKComponentFactory<Vector2f> {

  @Override
  public AbstractSDKComponent<Vector2f> create(Vector2f object, String propertyName) {
    return new Vector2fComponent(object, propertyName);
  }
}
