package com.jayfella.devkit.swing;

import com.github.weisj.darklaf.theme.Theme;

import javax.swing.*;
import java.awt.*;

public class ThemeComboBoxCellRenderer implements ListCellRenderer<Class<? extends Theme>> {

    private final DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

    @Override
    public Component getListCellRendererComponent(JList<? extends Class<? extends Theme>> list, Class<? extends Theme> value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);

        renderer.setText(value.getSimpleName().replace("Theme", ""));
        return renderer;
    }

}
