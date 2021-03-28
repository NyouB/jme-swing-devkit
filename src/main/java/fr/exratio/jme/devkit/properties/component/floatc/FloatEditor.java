package fr.exratio.jme.devkit.properties.component.floatc;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import fr.exratio.jme.devkit.properties.component.AbstractPropertyEditor;
import fr.exratio.jme.devkit.properties.component.FloatFormatFactory;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

public class FloatEditor extends AbstractPropertyEditor<Float> {

  public static final String LISTENED_PROPERTY_NAME = "value";

  private JPanel contentPanel;
  private JFormattedTextField valueTextField;


  public FloatEditor() {
    this(0f);
  }

  public FloatEditor(Float value) {
    super(value);
    $$$setupUI$$$();
  }

  protected Float computeValue() {
    return ((Number) valueTextField.getValue()).floatValue();
  }


  @Override
  public void setTypedValue(Float newValue) {
    if (newValue == null) {
      newValue = 0f;
    }
    this.valueTextField.setValue(newValue);
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
    contentPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
    contentPanel.add(valueTextField, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
  }


  public JComponent $$$getRootComponent$$$() {
    return contentPanel;
  }


  private void createUIComponents() {
    contentPanel = this;
    FloatFormatFactory floatFormatFactory = new FloatFormatFactory();
    valueTextField = new JFormattedTextField(floatFormatFactory, value);
    valueTextField.addPropertyChangeListener(LISTENED_PROPERTY_NAME, propertyChangeListener);
  }

}
