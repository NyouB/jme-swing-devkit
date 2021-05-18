package fr.exratio.jme.devkit.properties.component.integer;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import fr.exratio.jme.devkit.properties.component.AbstractPropertyEditor;
import fr.exratio.jme.devkit.properties.component.IntegerFormatFactory;
import java.awt.Insets;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

public class IntegerEditor extends AbstractPropertyEditor<Integer> {

  public static final String LISTENED_PROPERTY_NAME = "value";

  private JPanel contentPanel;

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JFormattedTextField valueTextField;

  public IntegerEditor() {
    this(0);
    initComponents();
  }

  @Override
  public void setTypedValue(Integer newValue) {
    if (newValue == null) {
      newValue = 0;
    }
    this.valueTextField.setValue(newValue);
    super.setTypedValue(newValue);
  }

  protected Integer computeValue() {
    return ((Number) valueTextField.getValue()).intValue();
  }


  private void createUIComponents() {
    contentPanel = this;
    valueTextField = new JFormattedTextField(new IntegerFormatFactory(), value);
    valueTextField.addPropertyChangeListener(LISTENED_PROPERTY_NAME, propertyChangeListener);
  }

  public IntegerEditor(Integer value) {
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
