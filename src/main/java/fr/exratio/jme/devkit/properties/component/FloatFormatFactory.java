package fr.exratio.jme.devkit.properties.component;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.InternationalFormatter;

public class FloatFormatFactory extends JFormattedTextField.AbstractFormatterFactory {

  private final float min;
  private final float max;

  public FloatFormatFactory() {
    this(Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  public FloatFormatFactory(float min, float max) {
    this.min = min;
    this.max = max;
  }

  @Override
  public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
    // Define the decimal factory.
    DecimalFormat format = new DecimalFormat(); // And here..
    format.setMinimumFractionDigits(1);
    format.setMaximumFractionDigits(7);
    format.setGroupingUsed(false);
    format.setRoundingMode(RoundingMode.HALF_UP);
    InternationalFormatter formatter = new InternationalFormatter(format);
    formatter.setAllowsInvalid(true);
    formatter.setMinimum(min);
    formatter.setMaximum(max);
    DefaultFormatterFactory factory2 = new DefaultFormatterFactory(formatter);

    return formatter;
  }


}
