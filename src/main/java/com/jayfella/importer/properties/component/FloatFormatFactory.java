package com.jayfella.importer.properties.component;

import javax.swing.*;
import javax.swing.text.InternationalFormatter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FloatFormatFactory extends JFormattedTextField.AbstractFormatterFactory {



    @Override
    public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {

        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(1);
        format.setMaximumFractionDigits(7);
        format.setGroupingUsed(false);
        format.setRoundingMode(RoundingMode.HALF_UP);

        InternationalFormatter formatter = new InternationalFormatter(format);
        formatter.setAllowsInvalid(true);
        formatter.setMinimum(-Float.MAX_VALUE);
        formatter.setMaximum(Float.MAX_VALUE);

        return formatter;
    }

}
