package com.jayfella.importer.properties;

import com.jayfella.importer.properties.component.SdkComponent;

public class PropertySection {

    private final String title;
    private final SdkComponent[] components;

    public PropertySection(String title, SdkComponent... components) {
        this.title = title;
        this.components = components;
    }

    public String getTitle() {
        return title;
    }

    public SdkComponent[] getComponents() {
        return components;
    }
}
