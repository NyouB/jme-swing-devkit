package fr.exratio.jme.devkit.properties.component.enumeration;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import fr.exratio.jme.devkit.properties.component.AbstractPropertyEditor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EnumSet;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class EnumEditor extends AbstractPropertyEditor<Enum> {

  private final Class<? extends Enum> clazz;
  private JPanel contentPanel;
  private JComboBox<Enum> valueComboBox;

  public EnumEditor(Class<Enum> clazz) {
    super(null);
    this.clazz = clazz;
    $$$setupUI$$$();
  }

  public EnumEditor(Enum value) {
    super(value);
    this.clazz = value.getClass();
    $$$setupUI$$$();
  }

  @Override
  public void setTypedValue(Enum newValue) {
    if (newValue == null) {
      newValue = valueComboBox.getItemAt(0);
    }
    valueComboBox.setSelectedItem(newValue);
    super.setTypedValue(newValue);
  }

  protected Enum<?> computeValue() {
    return (Enum<?>) valueComboBox.getSelectedItem();
  }


  /**
   * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
   * call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    createUIComponents();
    contentPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    contentPanel.add(valueComboBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, new Dimension(20, -1), new Dimension(80, -1), null, 0,
        false));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return contentPanel;
  }


  private void createUIComponents() {
    contentPanel = this;
    valueComboBox = new JComboBox(EnumSet.allOf(clazz).toArray());
    if (value == null) {
      valueComboBox.setSelectedIndex(0);
    } else {
      valueComboBox.setSelectedItem(value);
    }

    ItemListener itemListener = evt -> {
      if (ItemEvent.DESELECTED == evt.getStateChange()) {
        return;
      }
      Enum oldComponent = value;
      Enum<?> newComponent = computeValue();
      if (oldComponent == null || !oldComponent.equals(newComponent)) {
        setTypedValue(newComponent);
      }
    };
    valueComboBox.addItemListener(itemListener);
  }

}
