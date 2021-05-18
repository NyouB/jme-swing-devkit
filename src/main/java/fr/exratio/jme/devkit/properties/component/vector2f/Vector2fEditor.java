package fr.exratio.jme.devkit.properties.component.vector2f;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jme3.math.Vector2f;
import fr.exratio.jme.devkit.properties.component.AbstractPropertyEditor;
import fr.exratio.jme.devkit.properties.component.FloatFormatFactory;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Vector2fEditor extends AbstractPropertyEditor<Vector2f> {

  public static final String LISTENED_PROPERTY_NAME = "value";

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JFormattedTextField xTextField;
  private JFormattedTextField yTextField;

  @Override
  public void setTypedValue(Vector2f newValue) {
    if (newValue == null) {
      newValue = new Vector2f();
    }
    this.xTextField.setValue(newValue.x);
    this.yTextField.setValue(newValue.y);
    super.setTypedValue(newValue);
  }

  public void bind() {

  }

  protected Vector2f computeValue() {
    float x = ((Number) xTextField.getValue()).floatValue();
    float y = ((Number) yTextField.getValue()).floatValue();
    return new Vector2f(x, y);
  }


  private void createUIComponents() {
    FloatFormatFactory floatFormatFactory = new FloatFormatFactory();
    xTextField = new JFormattedTextField(floatFormatFactory, value.x);
    yTextField = new JFormattedTextField(floatFormatFactory, value.y);

    normalizeButton = new JButton();

    normalizeButton.addActionListener(e -> {
      Vector2f value = computeValue();
      Vector2f normalized = value.normalize();
      setTypedValue(normalized);
    });

    xTextField.addPropertyChangeListener(LISTENED_PROPERTY_NAME, propertyChangeListener);
    yTextField.addPropertyChangeListener(LISTENED_PROPERTY_NAME, propertyChangeListener);
  }

  private JButton normalizeButton;

  public Vector2fEditor() {
    this(new Vector2f());
    initComponents();
  }

  public Vector2fEditor(Vector2f vector2f) {
    super(vector2f);
    initComponents();
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    createUIComponents();

    var panel1 = new JPanel();
    var label1 = new JLabel();
    var label2 = new JLabel();

    //======== this ========
    setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));

    //======== panel1 ========
    {
      panel1.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));

      //---- label1 ----
      label1.setForeground(new Color(187, 24, 22));
      label1.setText("x");
      panel1.add(label1, new GridConstraints(0, 0, 1, 1,
          GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
          GridConstraints.SIZEPOLICY_FIXED,
          GridConstraints.SIZEPOLICY_FIXED,
          null, null, null));
      panel1.add(xTextField, new GridConstraints(0, 1, 1, 1,
          GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
          GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
          GridConstraints.SIZEPOLICY_FIXED,
          null, null, null));

      //---- label2 ----
      label2.setForeground(new Color(31, 187, 42));
      label2.setText("y");
      panel1.add(label2, new GridConstraints(1, 0, 1, 1,
          GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
          GridConstraints.SIZEPOLICY_FIXED,
          GridConstraints.SIZEPOLICY_FIXED,
          null, null, null));
      panel1.add(yTextField, new GridConstraints(1, 1, 1, 1,
          GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
          GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
          GridConstraints.SIZEPOLICY_FIXED,
          null, null, null));

      //---- normalizeButton ----
      normalizeButton.setText("normalize");
      panel1.add(normalizeButton, new GridConstraints(2, 1, 1, 1,
          GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
          GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
          GridConstraints.SIZEPOLICY_FIXED,
          null, null, null));
    }
    add(panel1, new GridConstraints(0, 0, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        null, null, null));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
