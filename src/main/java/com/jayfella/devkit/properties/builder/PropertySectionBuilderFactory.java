package com.jayfella.devkit.properties.builder;

public interface PropertySectionBuilderFactory<T> {

  AbstractPropertySectionBuilder<T> create(T object);
}
