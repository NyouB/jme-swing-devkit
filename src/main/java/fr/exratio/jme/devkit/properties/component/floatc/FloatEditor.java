package fr.exratio.jme.devkit.properties.component.floatc;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import fr.exratio.jme.devkit.properties.component.AbstractPropertyEditor;
import fr.exratio.jme.devkit.properties.component.FloatFormatFactory;
import java.awt.Insets;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

public class FloatEditor extends AbstractPropertyEditor<Float> {

  public static final String LISTENED_PROPERTY_NAME = "value";

  private JPanel contentPanel;


  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JFormattedTextField valueTextField;

  public FloatEditor() {
    this(0f);
    initComponents();
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


  private void createUIComponents() {
    contentPanel = this;
    FloatFormatFactory floatFormatFactory = new FloatFormatFactory();
    valueTextField = new JFormattedTextField(floatFormatFactory, value);
    valueTextField.addPropertyChangeListener(LISTENED_PROPERTY_NAME, propertyChangeListener);
  }


  public FloatEditor(Float value) {
    super(value);
    initComponents();
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    createUIComponents();

    //======== this ========
    setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
    add(valueTextField, new GridConstraints(0, 0, 1, 2,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
