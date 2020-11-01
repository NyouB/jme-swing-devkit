package com.jayfella.devkit.properties.builder;

import com.jayfella.devkit.properties.PropertySection;
import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jayfella.devkit.service.JmeEngineService;
import com.jayfella.devkit.service.ServiceManager;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.Builder;

public abstract class AbstractPropertySectionBuilder<T> implements Builder<List<PropertySection>> {

  protected final T object;
  protected final List<Field> ignoredFields;

  public AbstractPropertySectionBuilder(T object, Field... ignoredFieldsArray) {
    this.object = object;
    this.ignoredFields = Arrays.asList(ignoredFieldsArray.clone());
  }

  public void bind(Field field, AbstractSDKComponent component) {
    component.addPropertyChangeListener(
        value -> ServiceManager.getService(JmeEngineService.class).enqueue(() -> {
          try {
            field.set(object, value);
          } catch (IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
          }
        }));
  }

  public void cleanup() {

  }
}
