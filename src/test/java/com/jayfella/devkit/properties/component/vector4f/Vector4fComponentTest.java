package com.jayfella.devkit.properties.component.vector4f;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jayfella.devkit.properties.component.FloatFormatFactory;
import com.jayfella.devkit.properties.component.SwingTestCase;
import com.jme3.math.Vector4f;
import java.text.ParseException;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import org.junit.jupiter.api.Test;

class Vector4fComponentTest extends SwingTestCase {


  Vector4fEditor vector4fComponent;

  public static final void main(String[] args) throws ParseException {
    JFrame frame = new JFrame("Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new Vector4fEditor().getCustomEditor());
    frame.pack();
    frame.setVisible(true);
  }

  @Test
  void getJComponent() throws ParseException {
    FloatFormatFactory floatFormatFactory = new FloatFormatFactory();
    JFormattedTextField xTextField = new JFormattedTextField(floatFormatFactory);
    xTextField.setText("4,5");
    xTextField.commitEdit();
    assertEquals(4.5f, ((Number) xTextField.getValue()).floatValue());

    xTextField.setValue(8.f);
    assertEquals("8,0", xTextField.getText());
  }

  @Test
  void setComponent() {
    this.vector4fComponent = new Vector4fEditor();
    assertEquals(new Vector4f(), vector4fComponent.getValue());
    vector4fComponent.setTypedValue(new Vector4f(1, 2, 3, 4));
    assertEquals(1, vector4fComponent.getValue().x);
    assertEquals(2, vector4fComponent.getValue().y);
    assertEquals(3, vector4fComponent.getValue().z);
    assertEquals(4, vector4fComponent.getValue().w);
  }
}