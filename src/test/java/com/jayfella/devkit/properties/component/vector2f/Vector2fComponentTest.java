package com.jayfella.devkit.properties.component.vector2f;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jayfella.devkit.properties.component.vector4f.Vector4fComponent;
import com.jme3.math.Vector2f;
import java.text.ParseException;
import javax.swing.JFrame;
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

  public static final void main(String[] args) throws ParseException {
    JFrame frame = new JFrame("Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new Vector2fComponent().getJComponent());
    frame.pack();
    frame.setVisible(true);
  }
}