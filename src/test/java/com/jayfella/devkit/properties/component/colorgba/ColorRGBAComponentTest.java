package com.jayfella.devkit.properties.component.colorgba;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jayfella.devkit.properties.component.vector4f.Vector4fComponent;
import com.jme3.math.ColorRGBA;
import java.text.ParseException;
import javax.swing.JFrame;
import org.junit.jupiter.api.Test;

class ColorRGBAComponentTest {

  ColorRGBAComponent colorRGBAComponent;

  @Test
  void setComponent() {
    this.colorRGBAComponent = new ColorRGBAComponent();
    assertEquals(new ColorRGBA(), colorRGBAComponent.getComponent());
    colorRGBAComponent.setComponent(null);
    assertEquals(new ColorRGBA(), colorRGBAComponent.getComponent());
    colorRGBAComponent.setComponent(new ColorRGBA(1, 2, 3, 4));
    assertEquals(new ColorRGBA(1, 2, 3, 4), colorRGBAComponent.getComponent());
  }

  public static final void main(String[] args) throws ParseException {
    JFrame frame = new JFrame("Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new ColorRGBAComponent().getJComponent());
    frame.pack();
    frame.setVisible(true);
  }
}