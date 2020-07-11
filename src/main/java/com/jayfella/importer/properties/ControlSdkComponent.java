package com.jayfella.importer.properties;

import com.jayfella.importer.properties.component.SdkComponent;

public abstract class ControlSdkComponent<T> implements SdkComponent {

    protected final T object;

    public ControlSdkComponent(T object) {
        this.object = object;
    }

}
