package com.jayfella.devkit.properties.component.integer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import javax.swing.JFrame;
import org.junit.jupiter.api.Test;

class IntegerComponentTest {

  IntegerEditor integerComponent;

  public static final void main(String[] args) throws ParseException {
    JFrame frame = new JFrame("Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new IntegerEditor().getCustomEditor());
    frame.pack();
    frame.setVisible(true);
  }

  @Test
  void setComponent() {
    this.integerComponent = new IntegerEditor();
    assertEquals(0, integerComponent.getValue());
    integerComponent.setTypedValue(null);
    assertEquals(0, integerComponent.getValue());
    integerComponent.setTypedValue(4);
    assertEquals(4, integerComponent.getValue());
  }
}