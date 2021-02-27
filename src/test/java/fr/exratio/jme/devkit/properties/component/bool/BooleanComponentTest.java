package fr.exratio.jme.devkit.properties.component.bool;

import java.text.ParseException;
import javax.swing.JFrame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BooleanComponentTest {

  BooleanEditor booleanComponent;

  public static final void main(String[] args) throws ParseException {
    JFrame frame = new JFrame("Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new BooleanEditor().getCustomEditor());
    frame.pack();
    frame.setVisible(true);
  }

  @Test
  void setComponent() {
    this.booleanComponent = new BooleanEditor();
    Assertions.assertEquals(false, booleanComponent.getValue());
    booleanComponent.setTypedValue(null);
    Assertions.assertEquals(false, booleanComponent.getValue());
    booleanComponent.setTypedValue(true);
    Assertions.assertEquals(true, booleanComponent.getValue());
  }
}