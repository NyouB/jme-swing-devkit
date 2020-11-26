package com.jayfella.devkit.properties.component.floatc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import javax.swing.JFrame;
import org.junit.jupiter.api.Test;

class FloatComponentTest {

  FloatEditor floatComponent;

  public static final void main(String[] args) throws ParseException {
    JFrame frame = new JFrame("Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new FloatEditor().getCustomEditor());
    frame.pack();
    frame.setVisible(true);
  }

  @Test
  void setComponent() {
    this.floatComponent = new FloatEditor();
    assertEquals(0.0f, floatComponent.getValue());
    floatComponent.setTypedValue(null);
    assertEquals(0.0f, floatComponent.getValue());
    floatComponent.setTypedValue(4.5f);
    assertEquals(4.5f, floatComponent.getValue());
  }
}