package com.jayfella.devkit.properties.component.bool;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BooleanComponentTest {

  BooleanComponent booleanComponent;

  @Test
  void setComponent() {
    this.booleanComponent = new BooleanComponent();
    assertEquals(false, booleanComponent.getComponent());
    booleanComponent.setComponent(null);
    assertEquals(false, booleanComponent.getComponent());
    booleanComponent.setComponent(true);
    assertEquals(true, booleanComponent.getComponent());
  }
}