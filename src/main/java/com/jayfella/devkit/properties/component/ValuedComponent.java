package com.jayfella.devkit.properties.component;

public interface ValuedComponent<T> {

    void setValue(T value);

    PropertyChangedEvent getPropertyChangedEvent();
    void setPropertyChangedEvent(PropertyChangedEvent event);

}
