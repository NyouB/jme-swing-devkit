package com.jayfella.devkit.properties.component.vector3f;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jme3.math.Vector3f;
import org.junit.jupiter.api.Test;

class Vector3fComponentTest {

  private Vector3fComponent vector3fComponent;

  @Test
  void setComponent() {
    this.vector3fComponent = new Vector3fComponent();
    assertEquals(new Vector3f(), vector3fComponent.getComponent());
    vector3fComponent.setComponent(new Vector3f(1, 2, 3));
    assertEquals(1, vector3fComponent.getComponent().x);
    assertEquals(2, vector3fComponent.getComponent().y);
    assertEquals(3, vector3fComponent.getComponent().z);
  }
}