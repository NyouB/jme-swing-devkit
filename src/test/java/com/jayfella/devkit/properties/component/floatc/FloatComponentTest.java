package com.jayfella.devkit.properties.component.floatc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FloatComponentTest {

  FloatComponent floatComponent;

  @Test
  void setComponent() {
    this.floatComponent = new FloatComponent();
    assertEquals(0.0f, floatComponent.getComponent());
    floatComponent.setComponent(null);
    assertEquals(0.0f, floatComponent.getComponent());
    floatComponent.setComponent(4.5f);
    assertEquals(4.5f, floatComponent.getComponent());
  }
}