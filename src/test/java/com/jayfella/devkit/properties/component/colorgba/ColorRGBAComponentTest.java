package com.jayfella.devkit.properties.component.colorgba;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jme3.math.ColorRGBA;
import org.junit.jupiter.api.Test;

class ColorRGBAComponentTest {

  ColorRGBAComponent colorRGBAComponent;

  @Test
  void setComponent() {
    this.colorRGBAComponent = new ColorRGBAComponent();
    assertEquals(new ColorRGBA(), colorRGBAComponent.getComponent());
    colorRGBAComponent.setComponent(null);
    assertEquals(new ColorRGBA(), colorRGBAComponent.getComponent());
    colorRGBAComponent.setComponent(new ColorRGBA(1, 2, 3, 4));
    assertEquals(new ColorRGBA(1, 2, 3, 4), colorRGBAComponent.getComponent());
  }
}