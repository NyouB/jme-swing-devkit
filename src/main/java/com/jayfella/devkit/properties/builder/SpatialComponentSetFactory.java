package com.jayfella.devkit.properties.builder;

import com.jme3.scene.Spatial;

public class SpatialComponentSetFactory implements PropertySectionBuilderFactory<Spatial> {

  @Override
  public AbstractPropertySectionBuilder<Spatial> create(Spatial object) {
    return new SpatialPropertySectionBuilder(object);
  }
}
