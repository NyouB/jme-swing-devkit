package com.jayfella.devkit.properties.component.vector2f;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jme3.math.Vector2f;
import org.junit.jupiter.api.Test;

class Vector2fComponentTest {

  private Vector2fComponent vector2fComponent;

  @Test
  void setComponent() {
    this.vector2fComponent = new Vector2fComponent();
    assertEquals(new Vector2f(), vector2fComponent.getComponent());
    vector2fComponent.setComponent(new Vector2f(1, 2));
    assertEquals(1, vector2fComponent.getComponent().x);
    assertEquals(2, vector2fComponent.getComponent().y);
  }
}