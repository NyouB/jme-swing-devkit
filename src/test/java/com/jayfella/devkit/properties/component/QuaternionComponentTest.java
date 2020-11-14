package com.jayfella.devkit.properties.component;

import com.jayfella.devkit.properties.component.quaternion.QuaternionComponent;
import com.jme3.math.Quaternion;
import org.junit.jupiter.api.Assertions;

class QuaternionComponentTest extends AbstractJmeDevKitTest {

  QuaternionComponent quaternionComponent;


  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    Quaternion quaternion = new Quaternion();

    quaternionComponent = new QuaternionComponent(quaternion);
  }

  @org.junit.jupiter.api.Test
  void setComponent() {
    quaternionComponent.setComponent(new Quaternion(1, 2, 3, 4));
    Assertions.assertEquals(1, quaternionComponent.component.getX());
    Assertions.assertEquals(2, quaternionComponent.component.getY());
    Assertions.assertEquals(3, quaternionComponent.component.getZ());
    Assertions.assertEquals(4, quaternionComponent.component.getW());
  }

  @org.junit.jupiter.api.Test
  void setPropertyName() {
  }
}