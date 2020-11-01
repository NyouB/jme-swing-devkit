package com.jayfella.devkit.properties.component.quaternion;


import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jayfella.devkit.properties.component.SDKComponentFactory;
import com.jme3.math.Quaternion;

public class QuaternionComponentFactory implements SDKComponentFactory<Quaternion> {

  @Override
  public AbstractSDKComponent<Quaternion> create(Quaternion object) {
    return new QuaternionComponent(object);
  }
}
