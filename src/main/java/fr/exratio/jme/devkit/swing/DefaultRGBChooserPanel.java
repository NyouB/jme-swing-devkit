/* DefaultRGHChooserPanel.java --
   Copyright (C) 2004, 2005  Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */


package fr.exratio.jme.devkit.swing;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This is the default RGB panel for the JColorChooser. The color is selected using three sliders
 * that represent the RGB values.
 */
public class DefaultRGBChooserPanel extends AbstractColorChooserPanel {

  /**
   * The slider that handles the red values.
   */
  private transient JSlider redValueSlider;
  /**
   * The slider that handles the green values.
   */
  private transient JSlider greenValueSlider;

  /**
   * Whether the color change was initiated by the spinners.
   */
  private transient boolean spinnerChange = false;

  /**
   * Whether the color change was initiated by the sliders.
   */
  private transient boolean sliderChange = false;

  /**
   * Whether the change was forced by the chooser (meaning the color has already been changed).
   */
  private transient boolean updateChange = false;

  /**
   * The ChangeListener for the sliders.
   */
  private transient ChangeListener colorChanger;

  /**
   * The ChangeListener for the spinners.
   */
  private transient ChangeListener spinnerHandler;
  /**
   * The slider that handles the blue values.
   */
  private transient JSlider blueValueSlider;
  /**
   * The spinner that handles the red values.
   */
  private transient JSpinner redValueSpinner;
  /**
   * The spinner that handles the green values.
   */
  private transient JSpinner greenValueSpinner;
  /**
   * The spinner that handles the blue values.
   */
  private transient JSpinner blueValueSpinner;

  /**
   * Creates a new DefaultRGBChooserPanel object.
   */
  public DefaultRGBChooserPanel() {
    super();
  }

  /**
   * This method updates the chooser panel with the new color chosen in the JColorChooser.
   */
  public void updateChooser() {
    Color c = getColorFromModel();
    int rgb = c.getRGB();

    int red = rgb >> 16 & 0xff;
    int green = rgb >> 8 & 0xff;
    int blue = rgb & 0xff;

    updateChange = true;

    if (!sliderChange) {
      if (redValueSlider != null) {
        redValueSlider.setValue(red);
      }
      if (greenValueSlider != null) {
        greenValueSlider.setValue(green);
      }
      if (blueValueSlider != null) {
        blueValueSlider.setValue(blue);
      }
    }
    if (!spinnerChange) {
      if (greenValueSpinner != null) {
        greenValueSpinner.setValue(green);
      }
      if (redValueSpinner != null) {
        redValueSpinner.setValue(red);
      }
      if (blueValueSpinner != null) {
        blueValueSpinner.setValue(blue);
      }
    }

    updateChange = false;

    revalidate();
    repaint();
  }

  /**
   * This method builds the chooser panel.
   */
  protected void buildChooser() {
    setLayout(new GridBagLayout());

    JLabel RLabel = new JLabel("Red");
    RLabel.setDisplayedMnemonic('d');
    JLabel GLabel = new JLabel("Green");
    GLabel.setDisplayedMnemonic('n');
    JLabel BLabel = new JLabel("Blue");
    BLabel.setDisplayedMnemonic('B');

    redValueSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 255, 255);
    greenValueSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 255, 255);
    blueValueSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 255, 255);

    redValueSlider.setPaintTicks(true);
    redValueSlider.setSnapToTicks(false);
    greenValueSlider.setPaintTicks(true);
    greenValueSlider.setSnapToTicks(false);
    blueValueSlider.setPaintTicks(true);
    blueValueSlider.setSnapToTicks(false);

    redValueSlider.setLabelTable(redValueSlider.createStandardLabels(85));
    redValueSlider.setPaintLabels(true);
    greenValueSlider.setLabelTable(greenValueSlider.createStandardLabels(85));
    greenValueSlider.setPaintLabels(true);
    blueValueSlider.setLabelTable(blueValueSlider.createStandardLabels(85));
    blueValueSlider.setPaintLabels(true);

    redValueSlider.setMajorTickSpacing(85);
    greenValueSlider.setMajorTickSpacing(85);
    blueValueSlider.setMajorTickSpacing(85);

    redValueSlider.setMinorTickSpacing(17);
    greenValueSlider.setMinorTickSpacing(17);
    blueValueSlider.setMinorTickSpacing(17);

    redValueSpinner = new JSpinner(new SpinnerNumberModel(redValueSlider.getValue(),
        redValueSlider.getMinimum(),
        redValueSlider.getMaximum(), 1));
    greenValueSpinner = new JSpinner(new SpinnerNumberModel(greenValueSlider.getValue(),
        greenValueSlider.getMinimum(),
        greenValueSlider.getMaximum(), 1));
    blueValueSpinner = new JSpinner(new SpinnerNumberModel(blueValueSlider.getValue(),
        blueValueSlider.getMinimum(),
        blueValueSlider.getMaximum(), 1));

    RLabel.setLabelFor(redValueSlider);
    GLabel.setLabelFor(greenValueSlider);
    BLabel.setLabelFor(blueValueSlider);

    GridBagConstraints bag = new GridBagConstraints();
    bag.fill = GridBagConstraints.VERTICAL;

    bag.gridx = 0;
    bag.gridy = 0;
    add(RLabel, bag);

    bag.gridx = 1;
    add(redValueSlider, bag);

    bag.gridx = 2;
    add(redValueSpinner, bag);

    bag.gridx = 0;
    bag.gridy = 1;
    add(GLabel, bag);

    bag.gridx = 1;
    add(greenValueSlider, bag);

    bag.gridx = 2;
    add(greenValueSpinner, bag);

    bag.gridx = 0;
    bag.gridy = 2;
    add(BLabel, bag);

    bag.gridx = 1;
    add(blueValueSlider, bag);

    bag.gridx = 2;
    add(blueValueSpinner, bag);

    installListeners();
  }

  /**
   * This method returns the name displayed in the JTabbedPane.
   *
   * @return The name displayed in the JTabbedPane.
   */
  public String getDisplayName() {
    return "RGB";
  }

  /**
   * This method uninstalls the chooser panel from the JColorChooser.
   *
   * @param chooser The JColorChooser to remove this chooser panel from.
   */
  @Override
  public void uninstallChooserPanel(JColorChooser chooser) {
    uninstallListeners();
    removeAll();

    redValueSlider = null;
    greenValueSlider = null;
    blueValueSlider = null;

    redValueSpinner = null;
    greenValueSpinner = null;
    blueValueSpinner = null;

    super.uninstallChooserPanel(chooser);
  }

  /**
   * This method uninstalls any listeners that were added by the chooser panel.
   */
  private void uninstallListeners() {
    redValueSlider.removeChangeListener(colorChanger);
    greenValueSlider.removeChangeListener(colorChanger);
    blueValueSlider.removeChangeListener(colorChanger);

    colorChanger = null;

    redValueSpinner.removeChangeListener(spinnerHandler);
    greenValueSpinner.removeChangeListener(spinnerHandler);
    blueValueSpinner.removeChangeListener(spinnerHandler);

    spinnerHandler = null;
  }

  /**
   * This method installs any listeners that the chooser panel needs to operate.
   */
  private void installListeners() {
    colorChanger = new SliderHandler();

    redValueSlider.addChangeListener(colorChanger);
    greenValueSlider.addChangeListener(colorChanger);
    blueValueSlider.addChangeListener(colorChanger);

    spinnerHandler = new SpinnerHandler();

    redValueSpinner.addChangeListener(spinnerHandler);
    greenValueSpinner.addChangeListener(spinnerHandler);
    blueValueSpinner.addChangeListener(spinnerHandler);
  }

  /**
   * This method returns the small display icon.
   *
   * @return The small display icon.
   */
  public Icon getSmallDisplayIcon() {
    return null;
  }

  /**
   * This method returns the large display icon.
   *
   * @return The large display icon.
   */
  public Icon getLargeDisplayIcon() {
    return null;
  }

  /**
   * This class handles the slider value changes for all three sliders.
   */
  class SliderHandler implements ChangeListener {

    /**
     * This method is called whenever any of the slider values change.
     *
     * @param e The ChangeEvent.
     */
    public void stateChanged(ChangeEvent e) {
      if (updateChange) {
        return;
      }

      int color =
          redValueSlider.getValue() << 16 | greenValueSlider.getValue() << 8 | blueValueSlider
              .getValue();

      sliderChange = true;
      getColorSelectionModel().setSelectedColor(new Color(color));
      sliderChange = false;
    }
  }

  /**
   * This class handles the Spinner values changing.
   */
  class SpinnerHandler implements ChangeListener {

    /**
     * This method is called whenever any of the JSpinners change values.
     *
     * @param e The ChangeEvent.
     */
    public void stateChanged(ChangeEvent e) {
      if (updateChange) {
        return;
      }

      int red = ((Number) redValueSpinner.getValue()).intValue();
      int green = ((Number) greenValueSpinner.getValue()).intValue();
      int blue = ((Number) blueValueSpinner.getValue()).intValue();

      int color = red << 16 | green << 8 | blue;

      spinnerChange = true;
      getColorSelectionModel().setSelectedColor(new Color(color));
      spinnerChange = false;
    }
  }

}
