package com.jayfella.devkit.properties.component;

import com.jayfella.devkit.properties.reflection.ReflectedProperty;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ReflectedSdkComponent<T> implements SdkComponent, ValuedComponent<T>, NamedComponent {

    private String propertyName = "";

    private ReflectedProperty<T> reflectedProperty;
    private PropertyChangedEvent propertyChangedEvent;

    private final AtomicBoolean isBound = new AtomicBoolean(false);

    // is the object we're reflecting ever allowed to be NULL?
    private boolean nullable = false;

    // is the user allowed to set this value to null?
    // this could be a per-object basis. localTranslation = false, some material value = true.
    private boolean allowedNullable = false;

    public ReflectedSdkComponent(Object parent, Method getter, Method setter) {
        if (parent != null) {
            this.reflectedProperty = new ReflectedProperty<>(parent, getter, setter, this);
        }

    }

    public ReflectedProperty<T> getReflectedProperty() {
        return reflectedProperty;
    }

    public void setReflectedProperty(ReflectedProperty<T> reflectedProperty) {
        this.reflectedProperty = reflectedProperty;
    }

    protected boolean isBinded() {
        return isBound.get();
    }

    protected void setBound() {
        bind();

    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public void setValue(T value) {
        if (this.propertyChangedEvent != null) {
            this.propertyChangedEvent.propertyChanged(value);
            propertyChanged(value);
        }
    }

    @Override
    public PropertyChangedEvent getPropertyChangedEvent() {
        return this.propertyChangedEvent;
    }

    @Override
    public void setPropertyChangedEvent(PropertyChangedEvent event) {
        this.propertyChangedEvent = event;
    }



    /**
     * Internal use only.
     * Binds the input components to the object. Called AFTER the values have been set to avoid cyclic issues.
     */
    public void bind() {
        isBound.set(true);
    };

    public void propertyChanged(T value) {

    }

    @Override
    public boolean isNullable() {
        return nullable;
    }

    @Override
    public void setNullable(boolean value) {
        nullable = value;
    }

    @Override
    public void cleanup() {

    }

}
