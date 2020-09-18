package com.jayfella.devkit.properties.component;

import javax.swing.*;

public interface SdkComponent {

    JComponent getJComponent();

    boolean isNullable();
    void setNullable(boolean value);

    void cleanup();
}
