package com.jayfella.devkit.properties.component;

import com.jayfella.devkit.properties.component.quaternion.QuaternionComponent;
import com.jme3.math.Quaternion;
import org.junit.jupiter.api.Assertions;

class QuaternionComponentTest extends AbstractJmeDevKitTest{

  QuaternionComponent quaternionComponent;


  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    Quaternion quaternion = new Quaternion();

    quaternionComponent = new QuaternionComponent(quaternion);
  }

  @org.junit.jupiter.api.Test
  void setValue() {
    quaternionComponent.setComponent(new Quaternion(1,2,3,4));
    Assertions.assertEquals(1,((Number)quaternionComponent.getwTextField().getValue()).floatValue());
    Assertions.assertEquals(2,((Number)quaternionComponent.getxTextField().getValue()).floatValue());
    Assertions.assertEquals(3,((Number)quaternionComponent.getyTextField().getValue()).floatValue());
    Assertions.assertEquals(4,((Number)quaternionComponent.getzTextField().getValue()).floatValue());
  }

  @org.junit.jupiter.api.Test
  void bind() {
  }

  @org.junit.jupiter.api.Test
  void setPropertyName() {
  }
}