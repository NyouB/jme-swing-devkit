package com.jayfella.devkit.properties.component.colorgba;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jme3.math.ColorRGBA;
import java.text.ParseException;
import javax.swing.JFrame;
import org.junit.jupiter.api.Test;

class ColorRGBAComponentTest {

  ColorRGBAEditor colorRGBAComponent;

  public static final void main(String[] args) throws ParseException {
    JFrame frame = new JFrame("Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new ColorRGBAEditor().getCustomEditor());
    frame.pack();
    frame.setVisible(true);
  }

  @Test
  void setComponent() {
    this.colorRGBAComponent = new ColorRGBAEditor();
    assertEquals(new ColorRGBA(), colorRGBAComponent.getValue());
    colorRGBAComponent.setTypedValue(null);
    assertEquals(new ColorRGBA(), colorRGBAComponent.getValue());
    colorRGBAComponent.setTypedValue(new ColorRGBA(0.3f, 0.1f, 0.5f, 0.1f));
    assertEquals(new ColorRGBA(0.3f, 0.1f, 0.5f, 0.1f), colorRGBAComponent.getValue());
  }

  @Test
  void init() {
    this.colorRGBAComponent = new ColorRGBAEditor(null);
    assertEquals(new ColorRGBA(1, 1, 1, 1), colorRGBAComponent.getValue());
  }
}