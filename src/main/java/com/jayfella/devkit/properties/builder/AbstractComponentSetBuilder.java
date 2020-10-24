package com.jayfella.devkit.properties.builder;

import com.jayfella.devkit.properties.ComponentSetBuilder;
import com.jayfella.devkit.service.JmeEngineService;
import com.jayfella.devkit.service.ServiceManager;
import java.awt.Component;
import java.lang.reflect.Field;

public abstract class AbstractComponentSetBuilder<T> implements ComponentSetBuilder<T>{

    protected final T object;
    protected final String[] ignoredProperties;

    public AbstractComponentSetBuilder(T object, String... ignoredProperties) {
        this.object = object;
        this.ignoredProperties = ignoredProperties;
    }

    public void bind(Field field, Component component){
        component.addPropertyChangeListener(value -> {
            ServiceManager.getService(JmeEngineService.class).enqueue(() -> {
                try {
                    field.set(object, value);
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    e.printStackTrace();
                }
            });
        });
    }

}
