package com.jayfella.devkit.properties.component.string;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jayfella.devkit.properties.component.vector4f.Vector4fComponent;
import java.text.ParseException;
import javax.swing.JFrame;
import org.junit.jupiter.api.Test;

class StringComponentTest {

  private StringComponent stringComponent;

  @Test
  void setComponent() {
    this.stringComponent = new StringComponent();
    assertEquals("", stringComponent.getComponent());
    stringComponent.setComponent("myString");
    assertEquals("myString", stringComponent.getComponent());
  }

  public static final void main(String[] args) throws ParseException {
    JFrame frame = new JFrame("Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new StringComponent().getJComponent());
    frame.pack();
    frame.setVisible(true);
  }
}