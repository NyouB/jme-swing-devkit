package com.jayfella.devkit.properties.component.bool;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jayfella.devkit.properties.component.vector4f.Vector4fComponent;
import java.text.ParseException;
import javax.swing.JFrame;
import org.junit.jupiter.api.Test;

class BooleanComponentTest {

  BooleanComponent booleanComponent;

  @Test
  void setComponent() {
    this.booleanComponent = new BooleanComponent();
    assertEquals(false, booleanComponent.getComponent());
    booleanComponent.setComponent(null);
    assertEquals(false, booleanComponent.getComponent());
    booleanComponent.setComponent(true);
    assertEquals(true, booleanComponent.getComponent());
  }

  public static final void main(String[] args) throws ParseException {
    JFrame frame = new JFrame("Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new BooleanComponent().getJComponent());
    frame.pack();
    frame.setVisible(true);
  }
}