package com.jayfella.devkit.properties.component.texture2d;


import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jayfella.devkit.properties.component.SDKComponentFactory;
import com.jme3.texture.Texture2D;

public class Texture2DComponentFactory implements SDKComponentFactory<Texture2D> {

  @Override
  public AbstractSDKComponent<Texture2D> create(Texture2D object) {
    return new Texture2DComponent(object);
  }
}
