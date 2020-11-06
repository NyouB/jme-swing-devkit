package com.jayfella.devkit.properties.component.quaternion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jme3.math.Quaternion;
import org.junit.jupiter.api.Test;

class QuaternionComponentTest {

  QuaternionComponent quaternionComponent;

  @Test
  void setComponent() {
    this.quaternionComponent = new QuaternionComponent();
    assertEquals(new Quaternion(), quaternionComponent.getComponent());
    quaternionComponent.setComponent(new Quaternion(1, 2, 3, 4));
    assertEquals(1, quaternionComponent.getComponent().getX());
    assertEquals(2, quaternionComponent.getComponent().getY());
    assertEquals(3, quaternionComponent.getComponent().getZ());
    assertEquals(4, quaternionComponent.getComponent().getW());
  }
}