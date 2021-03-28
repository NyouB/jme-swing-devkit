package fr.exratio.jme.devkit.properties.component.string;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import fr.exratio.jme.devkit.properties.component.AbstractPropertyEditor;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StringEditor extends AbstractPropertyEditor<String> {

  private JTextField valueTextField;
  private JPanel contentPanel;

  public StringEditor() {
    this("");
  }

  public StringEditor(String value) {
    super(value);
    $$$setupUI$$$();
  }

  @Override
  public void setTypedValue(String newValue) {
    if (newValue == null) {
      newValue = "";
    }
    this.valueTextField.setText(value);
    super.setTypedValue(newValue);
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
    contentPanel.add(valueTextField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return contentPanel;
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
}
