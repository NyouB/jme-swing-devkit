package com.jayfella.devkit.properties.component.enumeration;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EnumSet;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class EnumComponent extends AbstractSDKComponent<Enum> {

  private JPanel contentPanel;
  private JComboBox<Enum> valueComboBox;
  private JLabel propertyNameLabel;

  public EnumComponent(Enum value) {
    this(value, null);
  }

  public EnumComponent(Enum value, String propertyName) {
    super(value, propertyName);
    $$$setupUI$$$();
  }

  @Override
  public void setComponent(Enum value) {
    if (value == null) {
      value = valueComboBox.getItemAt(0);
    }
    component = value;
    valueComboBox.setSelectedItem(component);
  }

  protected Enum<?> computeValue() {
    return (Enum<?>) valueComboBox.getSelectedItem();
  }

  @Override
  public void setPropertyName(String propertyName) {
    super.setPropertyName(propertyName);
    propertyNameLabel.setText("Enum: " + propertyName);
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
    contentPanel.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
    propertyNameLabel = new JLabel();
    propertyNameLabel.setText("Enum");
    contentPanel.add(propertyNameLabel,
        new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    final Spacer spacer1 = new Spacer();
    contentPanel.add(spacer1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0,
        false));
    contentPanel.add(valueComboBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JSeparator separator1 = new JSeparator();
    contentPanel.add(separator1,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null,
            null, 0, false));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return contentPanel;
  }

  private void createUIComponents() {
    valueComboBox = new JComboBox(EnumSet.allOf(component.getClass()).toArray());
    valueComboBox.setSelectedItem(component);

    ItemListener itemListener = evt -> {
      if (ItemEvent.DESELECTED == evt.getStateChange()) {
        return;
      }
      Enum oldComponent = component;
      Enum<?> newComponent = computeValue();
      if (!oldComponent.equals(newComponent)) {
        setComponent(newComponent);
        firePropertyChange(propertyName, oldComponent, newComponent);
      }
    };
    valueComboBox.addItemListener(itemListener);
  }

  @Override
  public JComponent getJComponent() {
    return contentPanel;
  }
}