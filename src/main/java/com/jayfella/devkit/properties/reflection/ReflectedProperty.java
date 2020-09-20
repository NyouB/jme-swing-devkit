package com.jayfella.devkit.properties.reflection;

import com.jayfella.devkit.properties.component.ValuedComponent;
import com.jayfella.devkit.service.JmeEngineService;
import com.jayfella.devkit.service.ServiceManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectedProperty<T> {

    private final Object parent;

    private final Method getter;
    private final Method setter;
    private final ValuedComponent<T> component;

    public ReflectedProperty(Object parent, Method getter, Method setter, ValuedComponent<T> component) {
        this.parent = parent;
        this.getter = getter;
        this.setter = setter;
        this.component = component;

        component.setPropertyChangedEvent(value -> {
            ServiceManager.getService(JmeEngineService.class).enqueue(() -> {
                try {
                    setter.invoke(parent, value);

                } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                    e.printStackTrace();
                }
            });
        });

    }

    public void update() {
        try {
            @SuppressWarnings("unchecked")
            T value = (T)getter.invoke(parent);
            this.component.setValue(value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void setValue(Object value) {
        try {
            setter.invoke(parent, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public T getValue() {
        try {
            @SuppressWarnings("unchecked")
            T value = (T)getter.invoke(parent);
            return value;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

}
