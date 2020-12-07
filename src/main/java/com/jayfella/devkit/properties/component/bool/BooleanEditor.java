package com.jayfella.devkit.properties.component.bool;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jayfella.devkit.properties.component.AbstractPropertyEditor;
import java.awt.Component;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class BooleanEditor extends AbstractPropertyEditor<Boolean> {

  public static final String LISTENED_PROPERTY_NAME = "selected";

  private JCheckBox checkBox;
  private JPanel contentPanel;

  public BooleanEditor() {
    this(false);
  }

  public BooleanEditor(Boolean bool) {
    super(bool != null && bool);
    $$$setupUI$$$();
  }

  @Override
  public void setTypedValue(Boolean newValue) {
    this.checkBox.setSelected(value);
    super.setTypedValue(newValue != null && newValue);
  }

  protected Boolean computeValue() {
    return checkBox.isSelected();
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
    checkBox.setText("Enabled");
    contentPanel.add(checkBox,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return contentPanel;
  }


  private void createUIComponents() {
    checkBox = new JCheckBox();
    checkBox.setSelected(value);
    checkBox.addPropertyChangeListener(LISTENED_PROPERTY_NAME, propertyChangeListener);
  }
}
