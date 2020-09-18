package com.jayfella.devkit.properties;

import com.jayfella.devkit.properties.component.SdkComponent;

public abstract class ControlSdkComponent<T> implements SdkComponent {

    protected final T object;

    public ControlSdkComponent(T object) {
        this.object = object;
    }

}
