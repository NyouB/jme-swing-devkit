package com.jayfella.devkit.properties.component.quaternion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jme3.math.Quaternion;
import java.text.ParseException;
import javax.swing.JFrame;
import org.junit.jupiter.api.Test;

class QuaternionComponentTest {

  QuaternionEditor quaternionComponent;

  public static final void main(String[] args) throws ParseException {
    JFrame frame = new JFrame("Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new QuaternionEditor().getCustomEditor());
    frame.pack();
    frame.setVisible(true);
  }

  @Test
  void setComponent() {
    this.quaternionComponent = new QuaternionEditor();
    assertEquals(new Quaternion(), quaternionComponent.getValue());
    quaternionComponent.setTypedValue(new Quaternion(1, 2, 3, 4));
    assertEquals(1, quaternionComponent.getValue().getX());
    assertEquals(2, quaternionComponent.getValue().getY());
    assertEquals(3, quaternionComponent.getValue().getZ());
    assertEquals(4, quaternionComponent.getValue().getW());
  }
}