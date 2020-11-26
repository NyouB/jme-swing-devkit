package com.jayfella.devkit.properties.builder;

import com.jayfella.devkit.properties.PropertySection;
import com.jayfella.devkit.properties.component.AbstractPropertyEditor;
import com.jayfella.devkit.properties.component.SDKComponentFactory;
import com.jayfella.devkit.properties.component.SdkComponent;
import com.jayfella.devkit.service.RegistrationService;
import com.jayfella.devkit.service.ServiceManager;
import com.jayfella.devkit.service.inspector.ControlFinder;
import com.jayfella.devkit.service.inspector.ExactMatchFinder;
import com.jayfella.devkit.service.inspector.InheritedMatchFinder;
import com.jayfella.devkit.service.inspector.PropertySectionListBuilder;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectedPropertySectionBuilder extends AbstractPropertySectionBuilder<Object> {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ReflectedPropertySectionBuilder.class);

  private static final Field classRefField = FieldUtils
      .getField(PropertyDescriptor.class, "classRef", true);
  private final String name;
  private final PropertySectionListBuilder propertySectionListBuilder;
  private final Map<Class<?>, Set<PropertyDescriptor>> groupedDescriptors;

  public ReflectedPropertySectionBuilder(String name, Object object,
      Field... ignoredFieldsArray) {
    super(object, ignoredFieldsArray);
    groupedDescriptors = getPropertyDescriptorGroupByClass(object);
    this.name = name;
    propertySectionListBuilder = new ControlFinder();
    propertySectionListBuilder.chainWith(new ExactMatchFinder())
        .chainWith(new InheritedMatchFinder());
  }

  public static Map<Class<?>, Set<PropertyDescriptor>> getPropertyDescriptorGroupByClass(
      Object bean) {
    PropertyDescriptor[] descriptors;
    try {
      descriptors = Introspector.getBeanInfo(bean.getClass())
          .getPropertyDescriptors();
    } catch (IntrospectionException e) {
      e.printStackTrace();
      return Collections.emptyMap();
    }
    Map<Class<?>, Set<PropertyDescriptor>> map = Arrays.stream(descriptors)
        .collect(Collectors.groupingBy(descriptor -> ReflectedPropertySectionBuilder
            .getPropertyDescriptorClassReference(descriptor), Collectors.toSet()));
    return map;
  }

  public static Class<?> getPropertyDescriptorClassReference(
      PropertyDescriptor propertyDescriptor) {
    WeakReference<?> classRef = null;
    try {
      classRef = (WeakReference<?>) classRefField.get(propertyDescriptor);
    } catch (IllegalAccessException e) {
      //should not happen add loger when not in test
      System.out.println("IllegalAccessException");
    }
    Class<?> propertyClassRef = (Class<?>) classRef.get();
    return propertyClassRef;
  }

  @Override
  public List<PropertySection> build() {
    LOGGER.trace(">> build()");
    List<PropertySection> result = new ArrayList<>();
    //One PropertySection by class level
    for (Class classLevel : groupedDescriptors.keySet()) {
      PropertySection propertySection = new PropertySection(classLevel.getSimpleName());
      for (PropertyDescriptor descriptor : groupedDescriptors.get(classLevel)) {
        RegistrationService registrationService = ServiceManager
            .getService(RegistrationService.class);
        AbstractPropertyEditor editor = registrationService
            .getEditorFor(descriptor.getPropertyType());
      }
    }
    for (ClassProperties classProperty : classPropertiesList) {
      PropertySection propertySection = new PropertySection(classProperty.clazz.getSimpleName());
      Map<String, Object> propertiesMap = classProperty.properties;
      for (Entry<String, Object> entry : propertiesMap.entrySet()) {
        List<PropertySection> propertySectionList = propertySectionListBuilder
            .find(entry.getValue().getClass(), entry.getValue(), entry.getKey());

        for (PropertySection ps : propertySectionList) {
          List<SdkComponent> components = ps
              .getComponents();
          for (SdkComponent component : components) {
            if (component instanceof AbstractPropertyEditor) {
              Field entryField = FieldUtils.getField(object.getClass(), entry.getKey());
              bind(entryField, (AbstractPropertyEditor) component);
            }
            propertySection.addProperty(component);
          }
        }
      }
      result.add(propertySection);
    }

    return result;
  }

  public AbstractPropertyEditor buildComponentFromField(SDKComponentFactory factory, Field field,
      Object fieldObject) {

    AbstractPropertyEditor component = factory.create(fieldObject, field.getName());
    bind(field, component);
    component.setPropertyName(field.getName());
    return component;
  }

  public static class ClassProperties {

    private Class clazz;
    private Map<String, Object> properties;

    public ClassProperties(Class clazz, Map<String, Object> properties) {
      this.clazz = clazz;
      this.properties = properties;
    }

    public ClassProperties(Class clazz) {
      this();
      this.clazz = clazz;
    }

    public ClassProperties() {
      properties = new HashMap<>();
    }

    public void addProperty(String name, Object object) {
      properties.put(name, object);
    }

    public void remove(String name) {
      properties.remove(name);
    }

    public Class getClazz() {
      return clazz;
    }

    public void setClazz(Class clazz) {
      this.clazz = clazz;
    }

    public Map<String, Object> getProperties() {
      return properties;
    }

    public void setProperties(Map<String, Object> properties) {
      this.properties = properties;
    }
  }

}
