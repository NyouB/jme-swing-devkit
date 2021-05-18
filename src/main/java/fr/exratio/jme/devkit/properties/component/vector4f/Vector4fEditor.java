package fr.exratio.jme.devkit.properties.component.vector4f;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jme3.math.Vector4f;
import fr.exratio.jme.devkit.properties.component.AbstractPropertyEditor;
import fr.exratio.jme.devkit.properties.component.FloatFormatFactory;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Vector4fEditor extends AbstractPropertyEditor<Vector4f> {

  public static final String LISTENED_PROPERTY_NAME = "value";

  private JPanel contentPanel;

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JFormattedTextField xTextField;
  private JFormattedTextField yTextField;

  @Override
  public void setTypedValue(Vector4f newValue) {
    if (newValue == null) {
      newValue = new Vector4f();
    }
    this.wTextField.setValue(newValue.w);
    this.xTextField.setValue(newValue.x);
    this.yTextField.setValue(newValue.y);
    this.zTextField.setValue(newValue.z);
    super.setTypedValue(newValue);
  }

  protected Vector4f computeValue() {
    float w = ((Number) wTextField.getValue()).floatValue();
    float x = ((Number) xTextField.getValue()).floatValue();
    float y = ((Number) yTextField.getValue()).floatValue();
    float z = ((Number) zTextField.getValue()).floatValue();
    return new Vector4f(x, y, z, w);
  }


  private void createUIComponents() {
    contentPanel = this;
    FloatFormatFactory floatFormatFactory = new FloatFormatFactory();
    wTextField = new JFormattedTextField(floatFormatFactory, value.w);
    xTextField = new JFormattedTextField(floatFormatFactory, value.x);
    yTextField = new JFormattedTextField(floatFormatFactory, value.y);
    zTextField = new JFormattedTextField(floatFormatFactory, value.z);
    wTextField.addPropertyChangeListener(LISTENED_PROPERTY_NAME, propertyChangeListener);
    xTextField.addPropertyChangeListener(LISTENED_PROPERTY_NAME, propertyChangeListener);
    yTextField.addPropertyChangeListener(LISTENED_PROPERTY_NAME, propertyChangeListener);
    zTextField.addPropertyChangeListener(LISTENED_PROPERTY_NAME, propertyChangeListener);
  }

  private JFormattedTextField zTextField;
  private JFormattedTextField wTextField;

  public Vector4fEditor() {
    this(new Vector4f());
    initComponents();
  }

  public Vector4fEditor(Vector4f vector4f) {
    super(vector4f);
    initComponents();
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    createUIComponents();

    var label1 = new JLabel();
    var label2 = new JLabel();
    var label3 = new JLabel();
    var label4 = new JLabel();

    //======== this ========
    setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));

    //---- label1 ----
    label1.setForeground(new Color(187, 24, 22));
    label1.setHorizontalAlignment(SwingConstants.CENTER);
    label1.setText("x");
    add(label1, new GridConstraints(0, 0, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    add(xTextField, new GridConstraints(0, 1, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));

    //---- label2 ----
    label2.setForeground(new Color(31, 187, 42));
    label2.setText("y");
    add(label2, new GridConstraints(1, 0, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    add(yTextField, new GridConstraints(1, 1, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));

    //---- label3 ----
    label3.setForeground(new Color(49, 168, 187));
    label3.setText("z");
    add(label3, new GridConstraints(2, 0, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    add(zTextField, new GridConstraints(2, 1, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));

    //---- label4 ----
    label4.setText("w");
    add(label4, new GridConstraints(3, 0, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));

    //---- wTextField ----
    wTextField.setText("");
    add(wTextField, new GridConstraints(3, 1, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
