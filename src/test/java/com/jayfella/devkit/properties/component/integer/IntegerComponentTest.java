package com.jayfella.devkit.properties.component.integer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jayfella.devkit.properties.component.vector4f.Vector4fComponent;
import java.text.ParseException;
import javax.swing.JFrame;
import org.junit.jupiter.api.Test;

class IntegerComponentTest {

  IntegerComponent integerComponent;

  @Test
  void setComponent() {
    this.integerComponent = new IntegerComponent();
    assertEquals(0, integerComponent.getComponent());
    integerComponent.setComponent(null);
    assertEquals(0, integerComponent.getComponent());
    integerComponent.setComponent(4);
    assertEquals(4, integerComponent.getComponent());
  }

  public static final void main(String[] args) throws ParseException {
    JFrame frame = new JFrame("Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new IntegerComponent().getJComponent());
    frame.pack();
    frame.setVisible(true);
  }
}