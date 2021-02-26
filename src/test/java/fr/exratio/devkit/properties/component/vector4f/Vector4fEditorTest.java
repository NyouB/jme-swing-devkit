package fr.exratio.devkit.properties.component.vector4f;

import com.jme3.math.Vector4f;
import java.beans.PropertyEditorManager;
import java.text.ParseException;
import javax.swing.JFrame;

class Vector4fEditorTest {

  public static final void main(String[] args) throws ParseException {
    PropertyEditorManager.registerEditor(Vector4f.class, Vector4fEditor.class);
    JFrame frame = new JFrame("Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(PropertyEditorManager.findEditor(Vector4f.class).getCustomEditor());
    frame.pack();
    frame.setVisible(true);
  }

}