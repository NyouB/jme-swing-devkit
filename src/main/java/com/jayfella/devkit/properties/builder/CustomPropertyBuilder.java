package com.jayfella.devkit.properties.builder;

import com.jayfella.devkit.properties.PropertySection;
import com.jayfella.devkit.properties.component.AbstractPropertyEditor;
import com.jayfella.devkit.service.RegistrationService;
import com.jayfella.devkit.service.ServiceManager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomPropertyBuilder extends AbstractPropertySectionBuilder<Object> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomPropertyBuilder.class);

  private final List<Field> fields;

  private CustomPropertyBuilder(Object object, Field... ignoredFieldsArray) {
    super(object, ignoredFieldsArray);
    fields = FieldUtils.getAllFieldsList(object.getClass());
    fields.removeAll(ignoredFields);
  }

  public CustomPropertyBuilder addField(Field field) {
    fields.add(field);
    return this;
  }

  public CustomPropertyBuilder removeField(Field field) {
    fields.remove(field);
    return this;
  }

  @Override
  public List<PropertySection> build() {

    List<AbstractPropertyEditor> components = new ArrayList<>();

    for (Field field : fields) {
      try {
        AbstractPropertyEditor newComponent = buildComponentFromField(field);
        if (newComponent == null) {
          continue;
        }
        components.add(newComponent);
      } catch (IllegalAccessException e) {
        LOGGER.error(
            "Error happen while getting field value from field {} from object class {}. The field will be ignored and not map",
            field.getName(), object.getClass().getCanonicalName(), e);
        ignoredFields.add(field);
      }
    }
    fields.removeAll(ignoredFields);

    PropertySection propertySection = new PropertySection(object.getClass().getSimpleName(),
        components.toArray(new AbstractPropertyEditor[components.size()]));

    List<PropertySection> sections = new ArrayList<>();
    sections.add(propertySection);

    return sections;
  }

  public AbstractPropertyEditor buildComponentFromField(Field field)
      throws IllegalAccessException {
    Object fieldObject = field.get(object);
    AbstractPropertyEditor component = ServiceManager.getService(RegistrationService.class)
        .getComponentFactoryFor(fieldObject.getClass()).create(fieldObject, field.getName());
    if (component != null) {
      bind(field, component);
      component.setPropertyName(field.getName());
    }
    return component;
  }

}
