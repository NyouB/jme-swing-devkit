package com.jayfella.devkit.properties.builder;

import com.jayfella.devkit.properties.PropertySection;
import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jayfella.devkit.properties.component.SDKComponentFactory;
import com.jayfella.devkit.service.RegistrationService;
import com.jayfella.devkit.service.ServiceManager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectedPropertySectionBuilder extends AbstractPropertySectionBuilder<Object> {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ReflectedPropertySectionBuilder.class);

  private final List<Field> fields;

  private final String name;

  public ReflectedPropertySectionBuilder(String name, Object object, Field... ignoredFieldsArray) {
    super(object, ignoredFieldsArray);
    fields = FieldUtils.getAllFieldsList(object.getClass());
    fields.removeAll(ignoredFields);
    this.name = name;
  }

  public ReflectedPropertySectionBuilder(Object object, Field... ignoredFieldsArray) {
    this(object.getClass().getCanonicalName(), object, ignoredFieldsArray);
  }

  public ReflectedPropertySectionBuilder addField(Field field) {
    fields.add(field);
    return this;
  }

  public ReflectedPropertySectionBuilder removeField(Field field) {
    fields.remove(field);
    return this;
  }

  @Override
  public List<PropertySection> build() {
    List<PropertySection> result = new ArrayList<>();
    fields.removeAll(ignoredFields);
    for (Field field : fields) {
      try {
        field.setAccessible(true);
        Object fieldObject = field.get(object);
        SDKComponentFactory sdkComponentFactory = ServiceManager
            .getService(RegistrationService.class)
            .getComponentFactoryFor(fieldObject.getClass());
        if (sdkComponentFactory != null) {
          AbstractSDKComponent newComponent = buildComponentFromField(sdkComponentFactory, field,
              fieldObject);
          result.add(new PropertySection(field.getName(), newComponent));
        } else {
          PropertySectionBuilderFactory propertyBuilderFactory = ServiceManager
              .getService(RegistrationService.class)
              .getPropertySectionBuilderFactoryFor(fieldObject.getClass());
          if (propertyBuilderFactory != null) {
            AbstractPropertySectionBuilder<List<PropertySection>> builder = propertyBuilderFactory
                .create(fieldObject);
            List<PropertySection> propertySectionList = builder.build();
            result.addAll(propertySectionList);
          }
        }
      } catch (IllegalAccessException e) {
        LOGGER.error(
            "Error happen while getting field value from field {} from object class {}. The field will be ignored and not map",
            field.getName(), object.getClass().getCanonicalName(), e);
        ignoredFields.add(field);
      }
    }
    return result;
  }

  public AbstractSDKComponent buildComponentFromField(SDKComponentFactory factory, Field field,
      Object fieldObject) {

    AbstractSDKComponent component = factory.create(fieldObject, field.getName());
    bind(field, component);
    component.setPropertyName(field.getName());
    return component;
  }

}
