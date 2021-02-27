package fr.exratio.jme.devkit.properties.component.vector3f;

import com.jme3.math.Vector3f;
import java.text.ParseException;
import javax.swing.JFrame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Vector3fComponentTest {

  private Vector3fEditor vector3fComponent;

  public static final void main(String[] args) throws ParseException {
    JFrame frame = new JFrame("Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new Vector3fEditor().getCustomEditor());
    frame.pack();
    frame.setVisible(true);
  }

  @Test
  void setComponent() {
    this.vector3fComponent = new Vector3fEditor();
    Assertions.assertEquals(new Vector3f(), vector3fComponent.getValue());
    vector3fComponent.setTypedValue(new Vector3f(1, 2, 3));
    Assertions.assertEquals(1, vector3fComponent.getValue().x);
    Assertions.assertEquals(2, vector3fComponent.getValue().y);
    Assertions.assertEquals(3, vector3fComponent.getValue().z);
  }
}