package com.jayfella.importer.properties;

import java.util.List;

public interface ComponentSetBuilder<T> {

    void setObject(T object, String... ignoredProperties);
    List<PropertySection> build();

}
