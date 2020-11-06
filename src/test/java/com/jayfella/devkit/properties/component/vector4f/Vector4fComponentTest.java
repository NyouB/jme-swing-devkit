package com.jayfella.devkit.properties.component.vector4f;

import static org.junit.jupiter.api.Assertions.*;

import com.jayfella.devkit.properties.component.FloatFormatFactory;
import com.jayfella.devkit.properties.component.SwingTestCase;
import com.jme3.math.Vector4f;
import java.text.ParseException;
import javax.swing.JFormattedTextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Vector4fComponentTest  extends SwingTestCase {

  Vector4fComponent vector4fComponent;

  @Test
  void setComponent() {
    this.vector4fComponent = new Vector4fComponent();
    assertEquals(new Vector4f(), vector4fComponent.getComponent());
    vector4fComponent.setComponent(new Vector4f(1, 2, 3, 4));
    assertEquals(1, vector4fComponent.getComponent().x);
    assertEquals(2, vector4fComponent.getComponent().y);
    assertEquals(3, vector4fComponent.getComponent().z);
    assertEquals(4, vector4fComponent.getComponent().w);
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
}