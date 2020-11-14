package com.jayfella.devkit.properties.component.texture2d;


import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jayfella.devkit.properties.component.SDKComponentFactory;
import com.jme3.asset.AssetManager;
import com.jme3.texture.Texture2D;

public class Texture2DComponentFactory implements SDKComponentFactory<Texture2D> {

  private final AssetManager assetManager;
  private final String assetRootDirectory;

  public Texture2DComponentFactory(AssetManager assetManager, String assetRootDirectory){
    this.assetManager = assetManager;
    this.assetRootDirectory = assetRootDirectory;
  }

  @Override
  public AbstractSDKComponent<Texture2D> create(Texture2D object, String propertyName) {
    return new Texture2DComponent(object, propertyName, assetManager, assetRootDirectory);
  }
}
