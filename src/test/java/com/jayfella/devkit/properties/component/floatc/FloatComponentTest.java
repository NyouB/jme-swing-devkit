package com.jayfella.devkit.properties.component.floatc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jayfella.devkit.properties.component.vector4f.Vector4fComponent;
import java.text.ParseException;
import javax.swing.JFrame;
import org.junit.jupiter.api.Test;

class FloatComponentTest {

  FloatComponent floatComponent;

  @Test
  void setComponent() {
    this.floatComponent = new FloatComponent();
    assertEquals(0.0f, floatComponent.getComponent());
    floatComponent.setComponent(null);
    assertEquals(0.0f, floatComponent.getComponent());
    floatComponent.setComponent(4.5f);
    assertEquals(4.5f, floatComponent.getComponent());
  }

  public static final void main(String[] args) throws ParseException {
    JFrame frame = new JFrame("Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new FloatComponent().getJComponent());
    frame.pack();
    frame.setVisible(true);
  }
}