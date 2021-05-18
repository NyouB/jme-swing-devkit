package fr.exratio.jme.devkit.properties.component.enumeration;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import fr.exratio.jme.devkit.properties.component.AbstractPropertyEditor;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EnumSet;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class EnumEditor extends AbstractPropertyEditor<Enum> {

  private final Class<? extends Enum> clazz;
  private JPanel contentPanel;

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JComboBox<? extends Enum> valueComboBox;

  public EnumEditor(Class<Enum> clazz) {
    super(null);
    initComponents();
    this.clazz = clazz;
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


  public EnumEditor(Enum value) {
    super(value);
    initComponents();
    this.clazz = value.getClass();
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    createUIComponents();

    //======== this ========
    setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    add(valueComboBox, new GridConstraints(0, 0, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
