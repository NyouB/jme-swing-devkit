package com.jayfella.importer.properties.component;

public interface ValuedComponent {

    void setValue(Object value);

    PropertyChangedEvent getPropertyChangedEvent();
    void setPropertyChangedEvent(PropertyChangedEvent event);

}
