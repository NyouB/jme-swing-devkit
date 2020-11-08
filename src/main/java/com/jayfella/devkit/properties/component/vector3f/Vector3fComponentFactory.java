package com.jayfella.devkit.properties.component.vector3f;


import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jayfella.devkit.properties.component.SDKComponentFactory;
import com.jme3.math.Vector3f;

public class Vector3fComponentFactory implements SDKComponentFactory<Vector3f> {

  @Override
  public AbstractSDKComponent<Vector3f> create(Vector3f object ,String propertyName) {
    return new Vector3fComponent(object, propertyName);
  }
}
