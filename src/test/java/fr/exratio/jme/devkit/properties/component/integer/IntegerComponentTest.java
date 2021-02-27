package fr.exratio.jme.devkit.properties.component.integer;

import java.text.ParseException;
import javax.swing.JFrame;
import org.junit.jupiter.api.Assertions;
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
    Assertions.assertEquals(0, integerComponent.getValue());
    integerComponent.setTypedValue(null);
    Assertions.assertEquals(0, integerComponent.getValue());
    integerComponent.setTypedValue(4);
    Assertions.assertEquals(4, integerComponent.getValue());
  }
}