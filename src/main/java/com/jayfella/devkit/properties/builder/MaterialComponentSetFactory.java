package com.jayfella.devkit.properties.builder;

import com.jme3.material.Material;

public class MaterialComponentSetFactory implements PropertySectionBuilderFactory<Material> {

  @Override
  public AbstractPropertySectionBuilder<Material> create(Material object) {
    return new MaterialPropertySectionBuilder(object);
  }
}
