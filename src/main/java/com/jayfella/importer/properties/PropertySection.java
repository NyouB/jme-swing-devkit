package com.jayfella.importer.properties;

import com.jayfella.importer.properties.component.SdkComponent;

import javax.swing.*;

public class PropertySection {

    private final String title;
    private final Icon icon;
    private final SdkComponent[] components;

    public PropertySection(String title, SdkComponent... components) {
        this(title, null, components);
    }

    public PropertySection(String title, Icon icon, SdkComponent... components) {
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

    public SdkComponent[] getComponents() {
        return components;
    }

}
