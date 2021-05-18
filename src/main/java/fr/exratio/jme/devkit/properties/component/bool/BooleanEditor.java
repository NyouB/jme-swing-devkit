package fr.exratio.jme.devkit.properties.component.bool;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import fr.exratio.jme.devkit.properties.component.AbstractPropertyEditor;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class BooleanEditor extends AbstractPropertyEditor<Boolean> {

  public static final String LISTENED_PROPERTY_NAME = "selected";

  private JPanel contentPanel;

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JCheckBox checkBox;

  public BooleanEditor() {
    this(false);
    initComponents();
  }

  @Override
  public void setTypedValue(Boolean newValue) {
    this.checkBox.setSelected(value);
    super.setTypedValue(newValue != null && newValue);
  }

  protected Boolean computeValue() {
    return checkBox.isSelected();
  }


  private void createUIComponents() {
    contentPanel = this;
    checkBox = new JCheckBox();
    checkBox.setSelected(value);
    checkBox.addPropertyChangeListener(LISTENED_PROPERTY_NAME, propertyChangeListener);
  }

  public BooleanEditor(Boolean bool) {
    super(bool != null && bool);
    initComponents();
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    createUIComponents();

    //======== this ========
    setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));

    //---- checkBox ----
    checkBox.setText("Enabled");
    add(checkBox, new GridConstraints(0, 0, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
