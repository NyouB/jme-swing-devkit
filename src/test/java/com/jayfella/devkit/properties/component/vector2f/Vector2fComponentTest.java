package com.jayfella.devkit.properties.component.vector2f;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jme3.math.Vector2f;
import java.text.ParseException;
import javax.swing.JFrame;
import org.junit.jupiter.api.Test;

class Vector2fComponentTest {

  private Vector2fEditor vector2fComponent;

  public static final void main(String[] args) throws ParseException {
    JFrame frame = new JFrame("Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new Vector2fEditor().getCustomEditor());
    frame.pack();
    frame.setVisible(true);
  }

  @Test
  void setComponent() {
    this.vector2fComponent = new Vector2fEditor();
    assertEquals(new Vector2f(), vector2fComponent.getValue());
    vector2fComponent.setTypedValue(new Vector2f(1, 2));
    assertEquals(1, vector2fComponent.getValue().x);
    assertEquals(2, vector2fComponent.getValue().y);
  }
}