package com.jayfella.importer.properties.builder;

import com.jayfella.importer.properties.PropertySection;
import com.jayfella.importer.properties.component.ReflectedSdkComponent;
import com.jayfella.importer.properties.reflection.ComponentBuilder;
import com.jayfella.importer.properties.reflection.UniqueProperties;

import java.util.ArrayList;
import java.util.List;

public class ReflectedComponentSetBuilder extends AbstractComponentSetBuilder<Object> {

    private final String name;

    public ReflectedComponentSetBuilder(String name, Object object, String... ignoredProperties) {
        super(object, ignoredProperties);
        this.name = name;
    }

    @Override
    public List<PropertySection> build() {

        UniqueProperties uniqueProperties = new UniqueProperties(object, ignoredProperties);
        ComponentBuilder componentBuilder = new ComponentBuilder(uniqueProperties);

        componentBuilder.build();
        PropertySection propertySection = new PropertySection(name, componentBuilder.getSdkComponents().toArray(new ReflectedSdkComponent[0]));

        List<PropertySection> propertySections = new ArrayList<>();
        propertySections.add(propertySection);
        return propertySections;

    }
}
