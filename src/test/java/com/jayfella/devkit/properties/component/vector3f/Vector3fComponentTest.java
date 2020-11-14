package com.jayfella.devkit.properties.component.vector3f;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jayfella.devkit.properties.component.vector4f.Vector4fComponent;
import com.jme3.math.Vector3f;
import java.text.ParseException;
import javax.swing.JFrame;
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

  public static final void main(String[] args) throws ParseException {
    JFrame frame = new JFrame("Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new Vector3fComponent().getJComponent());
    frame.pack();
    frame.setVisible(true);
  }
}