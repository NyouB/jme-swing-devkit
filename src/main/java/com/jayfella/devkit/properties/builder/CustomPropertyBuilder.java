package com.jayfella.devkit.properties.builder;

import com.jayfella.devkit.properties.PropertySection;
import com.jayfella.devkit.properties.component.BooleanComponent;
import com.jayfella.devkit.properties.component.ColorRGBAComponent;
import com.jayfella.devkit.properties.component.EnumComponent;
import com.jayfella.devkit.properties.component.FloatComponent;
import com.jayfella.devkit.properties.component.IntegerComponent;
import com.jayfella.devkit.properties.component.JMEDevKitComponentSwingView;
import com.jayfella.devkit.properties.component.StringComponent;
import com.jayfella.devkit.properties.component.Vector3fComponent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.lang3.reflect.FieldUtils;

public class CustomPropertyBuilder extends AbstractComponentSetBuilder<Object> {

  private static final Logger log = Logger.getLogger(CustomPropertyBuilder.class.getName());

  private List<Field> fields = new ArrayList<>();

  public CustomPropertyBuilder(Object object, String... ignoredProperties) {
    super(object, ignoredProperties);
    fields = FieldUtils.getAllFieldsList(object.getClass());
  }

  public void addProperty(Field field) {
    fields.add(field);
  }

  @Override
  public List<PropertySection> build() {

    List<JMEDevKitComponentSwingView> components = new ArrayList<>();

    for (Field field : fields) {
      components.add(buildComponentFromField(field));
    }

    PropertySection propertySection = new PropertySection(object.getClass().getSimpleName(),
        components.toArray(new JMEDevKitComponentSwingView[components.size()]));

    List<PropertySection> sections = new ArrayList<>();
    sections.add(propertySection);

    return sections;
  }

  public JMEDevKitComponentSwingView buildComponentFromField(Field field) {
    Object fieldObject = null;
    try {
      fieldObject = field.get(object);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    JMEDevKitComponentSwingView component = null;

    if (fieldObject.getClass() == boolean.class) {
      component = new BooleanComponent((Boolean) fieldObject);
      bind(field, component);
    } else if (fieldObject.getClass() == ColorRGBA.class) {
      component = new ColorRGBAComponent((ColorRGBA) fieldObject);
      bind(field, component);
    } else if (fieldObject.getClass().isEnum()) {
      component = new EnumComponent((Enum<?>) fieldObject);
      bind(field, component);
    } else if (fieldObject.getClass() == float.class) {
      component = new FloatComponent((Float) fieldObject);
      bind(field, component);
    } else if (fieldObject.getClass() == int.class) {
      component = new IntegerComponent((Integer) fieldObject);
      bind(field, component);
    } else if (fieldObject.getClass() == String.class) {
      component = new StringComponent((String) fieldObject);
      bind(field, component);
    } else if (fieldObject.getClass() == Vector3f.class) {
      component = new Vector3fComponent((Vector3f) fieldObject);
      bind(field, component);
    } else {
      log.warning("Unable to create Property for: " + fieldObject.getClass());
    }

    if (component != null) {
      component.setPropertyName(field.getName());
    }
    return component;
  }

}
