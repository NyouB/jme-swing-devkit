package fr.exratio.jme.devkit.properties.component.quaternion;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jme3.math.Quaternion;
import fr.exratio.jme.devkit.properties.component.AbstractPropertyEditor;
import fr.exratio.jme.devkit.properties.component.FloatFormatFactory;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class QuaternionEditor extends AbstractPropertyEditor<Quaternion> {

  public static final String LISTENED_PROPERTY_NAME = "value";

  private JPanel contentPanel;

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JFormattedTextField xTextField;
  private JFormattedTextField yTextField;

  @Override
  public void setTypedValue(Quaternion newValue) {
    if (newValue == null) {
      newValue = new Quaternion();
    }
    this.xTextField.setValue(newValue.getX());
    this.yTextField.setValue(newValue.getY());
    this.zTextField.setValue(newValue.getZ());
    this.wTextField.setValue(newValue.getW());
    super.setTypedValue(newValue);
  }

  @Override
  protected Quaternion computeValue() {
    float w = ((Number) wTextField.getValue()).floatValue();
    float x = ((Number) xTextField.getValue()).floatValue();
    float y = ((Number) yTextField.getValue()).floatValue();
    float z = ((Number) zTextField.getValue()).floatValue();
    return new Quaternion(x, y, z, w);
  }


  private void createUIComponents() {
    contentPanel = this;
    FloatFormatFactory floatFormatFactory = new FloatFormatFactory();
    wTextField = new JFormattedTextField(floatFormatFactory, value.getW());
    xTextField = new JFormattedTextField(floatFormatFactory, value.getX());
    yTextField = new JFormattedTextField(floatFormatFactory, value.getY());
    zTextField = new JFormattedTextField(floatFormatFactory, value.getZ());
    wTextField.addPropertyChangeListener(LISTENED_PROPERTY_NAME, propertyChangeListener);
    xTextField.addPropertyChangeListener(LISTENED_PROPERTY_NAME, propertyChangeListener);
    yTextField.addPropertyChangeListener(LISTENED_PROPERTY_NAME, propertyChangeListener);
    zTextField.addPropertyChangeListener(LISTENED_PROPERTY_NAME, propertyChangeListener);
  }

  private JFormattedTextField zTextField;
  private JFormattedTextField wTextField;

  public QuaternionEditor() {
    this(new Quaternion());
    initComponents();
  }

  public QuaternionEditor(Quaternion quaternion) {
    super(quaternion);
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
    add(wTextField, new GridConstraints(3, 1, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
