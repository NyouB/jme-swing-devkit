package com.jayfella.devkit.properties.component.colorgba;

import com.github.weisj.darklaf.listener.MouseClickListener;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jayfella.devkit.core.ColorConverter;
import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jme3.math.ColorRGBA;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.TitledBorder;

public class ColorRGBAComponent extends AbstractSDKComponent<ColorRGBA> {

  public static final String LISTENED_PROPERTY_NAME = "background";

  private JPanel contentPanel;
  private JLabel propertyNameLabel;
  private JLabel colorValueLabel;
  private JPanel colorPanel;

  public ColorRGBAComponent() {
    this(new ColorRGBA(), null);
  }

  public ColorRGBAComponent(String propertyName) {
    this(new ColorRGBA(), propertyName);
  }

  public ColorRGBAComponent(ColorRGBA colorRGBA) {
    this(colorRGBA, null);
  }

  public ColorRGBAComponent(ColorRGBA colorRGBA, String propertyName) {
    super(colorRGBA, propertyName);
    if (colorRGBA == null) {
      component = new ColorRGBA();
    }
    $$$setupUI$$$();
  }

  @Override
  public void setComponent(ColorRGBA value) {
    if (value == null) {
      value = new ColorRGBA();
    }
    component = value;
    colorPanel.setBackground(ColorConverter.toColor(component));
    colorValueLabel.setText(String.format("[ %.2f, %.2f, %.2f, %.2f ]",
        component.r, component.g, component.b, component.a));
    colorPanel.repaint();
    colorPanel.revalidate();
  }


  @Override
  public void setPropertyName(String propertyName) {
    super.setPropertyName(propertyName);
    propertyNameLabel.setText("ColorRGBA: " + propertyName);
  }

  protected ColorRGBA computeValue() {
    return ColorConverter.toColorRGBA(colorPanel.getBackground());
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
    contentPanel.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
    contentPanel.add(colorPanel,
        new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
            new Dimension(24, 24), new Dimension(24, 24), new Dimension(24, 24), 0, false));
    colorPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null,
        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    colorValueLabel = new JLabel();
    colorValueLabel.setText("Label");
    contentPanel.add(colorValueLabel, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    propertyNameLabel = new JLabel();
    propertyNameLabel.setText("Label");
    contentPanel.add(propertyNameLabel,
        new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER,
            GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JSeparator separator1 = new JSeparator();
    contentPanel.add(separator1,
        new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
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
    colorPanel = new JPanel();
    colorPanel.setBackground(ColorConverter.toColor(component));
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
      setComponent(newValue);
    });
    colorPanel.addPropertyChangeListener(LISTENED_PROPERTY_NAME, propertyChangeListener);
  }

  @Override
  public JComponent getJComponent() {
    return contentPanel;
  }
}
