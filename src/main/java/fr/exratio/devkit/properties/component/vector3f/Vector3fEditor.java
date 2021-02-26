package fr.exratio.devkit.properties.component.vector3f;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jme3.math.Vector3f;
import fr.exratio.devkit.properties.component.AbstractPropertyEditor;
import fr.exratio.devkit.properties.component.FloatFormatFactory;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Vector3fEditor extends AbstractPropertyEditor<Vector3f> {

  public static final String LISTENED_PROPERTY_NAME = "value";

  private JPanel contentPanel;
  private JFormattedTextField xTextField;
  private JFormattedTextField yTextField;
  private JFormattedTextField zTextField;
  private JButton normalizeButton;

  public Vector3fEditor() {
    this(new Vector3f());
  }

  public Vector3fEditor(Vector3f vector3f) {
    super(vector3f);
    $$$setupUI$$$();
  }

  @Override
  public void setTypedValue(Vector3f newValue) {
    if (newValue == null) {
      newValue = new Vector3f();
    }
    this.xTextField.setValue(newValue.x);
    this.yTextField.setValue(newValue.y);
    this.zTextField.setValue(newValue.z);
    super.setTypedValue(newValue);
  }

  protected Vector3f computeValue() {
    float x = ((Number) xTextField.getValue()).floatValue();
    float y = ((Number) yTextField.getValue()).floatValue();
    float z = ((Number) zTextField.getValue()).floatValue();
    return new Vector3f(x, y, z);
  }

  @Override
  public Component getCustomEditor() {
    return contentPanel;
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
   * call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    createUIComponents();
    contentPanel = new JPanel();
    contentPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
    contentPanel.add(panel1,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    final JLabel label1 = new JLabel();
    label1.setForeground(new Color(-4515818));
    label1.setText("x");
    panel1.add(label1,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    panel1.add(xTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED, new Dimension(20, -1), new Dimension(80, -1), null, 0,
        false));
    panel1.add(yTextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED, new Dimension(20, -1), new Dimension(80, -1), null, 0,
        false));
    final JLabel label2 = new JLabel();
    label2.setForeground(new Color(-14697686));
    label2.setText("y");
    panel1.add(label2,
        new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    panel1.add(zTextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED, new Dimension(20, -1), new Dimension(80, -1), null, 0,
        false));
    final JLabel label3 = new JLabel();
    label3.setForeground(new Color(-13522757));
    label3.setText("z");
    panel1.add(label3,
        new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    normalizeButton.setBorderPainted(true);
    normalizeButton.setText("normalize");
    panel1.add(normalizeButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
  }


  public JComponent $$$getRootComponent$$$() {
    return contentPanel;
  }


  private void createUIComponents() {
    FloatFormatFactory floatFormatFactory = new FloatFormatFactory();
    xTextField = new JFormattedTextField(floatFormatFactory, value.x);
    yTextField = new JFormattedTextField(floatFormatFactory, value.y);
    zTextField = new JFormattedTextField(floatFormatFactory, value.z);

    normalizeButton = new JButton();

    normalizeButton.addActionListener(e -> {
      Vector3f value = computeValue();
      Vector3f normalized = value.normalize();
      setTypedValue(normalized);
    });

    xTextField.addPropertyChangeListener(LISTENED_PROPERTY_NAME, propertyChangeListener);
    yTextField.addPropertyChangeListener(LISTENED_PROPERTY_NAME, propertyChangeListener);
    zTextField.addPropertyChangeListener(LISTENED_PROPERTY_NAME, propertyChangeListener);
  }

}