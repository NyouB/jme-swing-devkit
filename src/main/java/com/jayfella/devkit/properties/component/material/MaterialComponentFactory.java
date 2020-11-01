package com.jayfella.devkit.properties.component.material;


import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jayfella.devkit.properties.component.SDKComponentFactory;
import com.jme3.material.Material;

public class MaterialComponentFactory implements SDKComponentFactory<Material> {

  @Override
  public AbstractSDKComponent<Material> create(Material object) {
    return new MaterialChooserComponent(object);
  }
}
