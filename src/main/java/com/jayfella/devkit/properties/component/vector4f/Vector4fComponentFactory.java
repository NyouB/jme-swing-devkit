package com.jayfella.devkit.properties.component.vector4f;


import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jayfella.devkit.properties.component.SDKComponentFactory;
import com.jme3.math.Vector4f;

public class Vector4fComponentFactory implements SDKComponentFactory<Vector4f> {

  @Override
  public AbstractSDKComponent<Vector4f> create(Vector4f object) {
    return new Vector4fComponent(object);
  }
}
