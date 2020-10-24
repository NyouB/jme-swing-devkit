package com.jayfella.devkit.properties.component;

import javax.swing.*;
import javax.swing.text.InternationalFormatter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

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

        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(1);
        format.setMaximumFractionDigits(7);
        format.setGroupingUsed(false);
        format.setRoundingMode(RoundingMode.HALF_UP);

        InternationalFormatter formatter = new InternationalFormatter(format);
        formatter.setAllowsInvalid(true);
        formatter.setMinimum(min);
        formatter.setMaximum(max);

        return formatter;
    }

}
