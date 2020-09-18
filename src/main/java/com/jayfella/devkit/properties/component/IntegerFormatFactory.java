package com.jayfella.devkit.properties.component;

import javax.swing.*;
import javax.swing.text.InternationalFormatter;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class IntegerFormatFactory  extends JFormattedTextField.AbstractFormatterFactory {

    @Override
    public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {

        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        format.setGroupingUsed(false);
        // format.setRoundingMode(RoundingMode.HALF_UP);

        InternationalFormatter formatter = new InternationalFormatter(format);
        formatter.setAllowsInvalid(false);
        formatter.setMinimum(Integer.MIN_VALUE);
        formatter.setMaximum(Integer.MAX_VALUE);

        return formatter;
    }

}
