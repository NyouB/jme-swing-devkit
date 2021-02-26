package fr.exratio.devkit.properties.component.floatc;

import java.text.ParseException;
import javax.swing.JFrame;
import org.junit.jupiter.api.Assertions;
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
    Assertions.assertEquals(0.0f, floatComponent.getValue());
    floatComponent.setTypedValue(null);
    Assertions.assertEquals(0.0f, floatComponent.getValue());
    floatComponent.setTypedValue(4.5f);
    Assertions.assertEquals(4.5f, floatComponent.getValue());
  }
}