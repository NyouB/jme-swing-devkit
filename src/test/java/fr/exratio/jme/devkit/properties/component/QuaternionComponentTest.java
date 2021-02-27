package fr.exratio.jme.devkit.properties.component;

import com.jme3.math.Quaternion;
import fr.exratio.jme.devkit.properties.component.quaternion.QuaternionEditor;
import org.junit.jupiter.api.Assertions;

class QuaternionComponentTest extends AbstractJmeDevKitTest {

  QuaternionEditor quaternionComponent;


  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    Quaternion quaternion = new Quaternion();

    quaternionComponent = new QuaternionEditor(quaternion);
  }

  @org.junit.jupiter.api.Test
  void setComponent() {
    quaternionComponent.setTypedValue(new Quaternion(1, 2, 3, 4));
    Assertions.assertEquals(1, quaternionComponent.value.getX());
    Assertions.assertEquals(2, quaternionComponent.value.getY());
    Assertions.assertEquals(3, quaternionComponent.value.getZ());
    Assertions.assertEquals(4, quaternionComponent.value.getW());
  }

  @org.junit.jupiter.api.Test
  void setPropertyName() {
  }
}