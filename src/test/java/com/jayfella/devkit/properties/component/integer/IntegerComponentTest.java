package com.jayfella.devkit.properties.component.integer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class IntegerComponentTest {

  IntegerComponent integerComponent;

  @Test
  void setComponent() {
    this.integerComponent = new IntegerComponent();
    assertEquals(0, integerComponent.getComponent());
    integerComponent.setComponent(null);
    assertEquals(0, integerComponent.getComponent());
    integerComponent.setComponent(4);
    assertEquals(4, integerComponent.getComponent());
  }
}