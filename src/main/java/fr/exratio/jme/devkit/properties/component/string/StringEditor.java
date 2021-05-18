package fr.exratio.jme.devkit.properties.component.string;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import fr.exratio.jme.devkit.properties.component.AbstractPropertyEditor;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StringEditor extends AbstractPropertyEditor<String> {

  private JPanel contentPanel;

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JTextField valueTextField;

  public StringEditor() {
    this("");
    initComponents();
  }

  @Override
  public void setTypedValue(String newValue) {
    if (newValue == null) {
      newValue = "";
    }
    this.valueTextField.setText(value);
    super.setTypedValue(newValue);
  }


  @Override
  protected String computeValue() {
    return valueTextField.getText();
  }


  private void createUIComponents() {
    contentPanel = this;
    valueTextField = new JTextField(value);
    valueTextField.addPropertyChangeListener("value", propertyChangeListener);
  }

  public StringEditor(String value) {
    super(value);
    initComponents();
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    createUIComponents();

    //======== this ========
    setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    add(valueTextField, new GridConstraints(0, 0, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
