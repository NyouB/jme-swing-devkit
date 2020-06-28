package com.jayfella.importer.swing;

import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;

public class NumberFormatters {

    public static NumberFormatter createFloatFormatter(float min, float max) {

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);

        NumberFormatter floatFormatter = new NumberFormatter(numberFormat);
        floatFormatter.setMinimum(min);
        floatFormatter.setMaximum(max);
        floatFormatter.setAllowsInvalid(true);
        floatFormatter.setCommitsOnValidEdit(true);

        return floatFormatter;
    }

    public static NumberFormatter createIntegerFormatter(int min, int max) {

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        numberFormat.setParseIntegerOnly(true);

        NumberFormatter intFormatter = new NumberFormatter(numberFormat);
        intFormatter.setMinimum(min);
        intFormatter.setMaximum(max);
        intFormatter.setAllowsInvalid(true);
        intFormatter.setCommitsOnValidEdit(false);

        return intFormatter;
    }

}
