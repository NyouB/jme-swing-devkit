package fr.exratio.jme.devkit.properties.component.colorgba;

import com.github.weisj.darklaf.listener.MouseClickListener;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jme3.math.ColorRGBA;
import fr.exratio.jme.devkit.core.ColorConverter;
import fr.exratio.jme.devkit.properties.component.AbstractPropertyEditor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ColorRGBAEditor extends AbstractPropertyEditor<ColorRGBA> {

  public static final String LISTENED_PROPERTY_NAME = "background";

  private JPanel contentPanel;

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JPanel colorPanel;
  private JLabel colorValueLabel;

  @Override
  public void setTypedValue(ColorRGBA newValue) {
    if (newValue == null) {
      newValue = new ColorRGBA();
    }
    colorPanel.setBackground(ColorConverter.toColor(newValue));
    colorValueLabel.setText(String.format("[ %.2f, %.2f, %.2f, %.2f ]",
        newValue.r, newValue.g, newValue.b, newValue.a));
    colorPanel.repaint();
    colorPanel.revalidate();
    super.setTypedValue(newValue);
  }

  protected ColorRGBA computeValue() {
    return ColorConverter.toColorRGBA(colorPanel.getBackground());
  }


  private void createUIComponents() {
    contentPanel = this;
    colorPanel = new JPanel();
    colorPanel.setBackground(ColorConverter.toColor(value));
    colorPanel.addMouseListener((MouseClickListener) e -> {

      Color color = JColorChooser.showDialog(null,
          "Color",
          colorPanel.getBackground());

      if (color == null) {
        return;
      }
      ColorRGBA newValue = ColorConverter.toColorRGBA(color);
      colorValueLabel.setText(String.format("[ %.2f, %.2f, %.2f, %.2f ]",
          newValue.r, newValue.g, newValue.b, newValue.a));
      setTypedValue(newValue);
    });
    colorPanel.addPropertyChangeListener(LISTENED_PROPERTY_NAME, propertyChangeListener);
  }


  public ColorRGBAEditor() {
    this(new ColorRGBA());
    initComponents();
  }

  public ColorRGBAEditor(ColorRGBA colorRGBA) {
    super(colorRGBA);
    initComponents();
    if (colorRGBA == null) {
      value = new ColorRGBA();
    }
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    createUIComponents();

    colorValueLabel = new JLabel();

    //======== this ========
    setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));

    //======== colorPanel ========
    {
      colorPanel.setBorder(new TitledBorder(new EtchedBorder(), ""));
      colorPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    }
    add(colorPanel, new GridConstraints(0, 0, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
        GridConstraints.SIZEPOLICY_FIXED,
        GridConstraints.SIZEPOLICY_FIXED,
        new Dimension(24, 24), new Dimension(24, 24), new Dimension(24, 24)));

    //---- colorValueLabel ----
    colorValueLabel.setText("Label");
    add(colorValueLabel, new GridConstraints(0, 1, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
