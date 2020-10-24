package com.jayfella.devkit.properties;

import java.awt.Component;
import javax.swing.*;

public class PropertySection {

    private final String title;
    private final Icon icon;
    private final Component[] components;

    public PropertySection(String title, Component... components) {
        this(title, null, components);
    }

    public PropertySection(String title, Icon icon, Component... components) {
        this.title = title;
        this.icon = icon;
        this.components = components;
    }

    public String getTitle() {
        return title;
    }

    public Icon getIcon() {
        return icon;
    }

    public Component[] getComponents() {
        return components;
    }

}
