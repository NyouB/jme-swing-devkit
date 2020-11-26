package com.jayfella.devkit.properties.builder;

import com.jme3.scene.Geometry;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ReflectedPropertySectionBuilderTest {

  @Test
  void descriptorsArrayToMapTest() throws IntrospectionException {
    Geometry geometry = new Geometry();
    PropertyDescriptor[] descriptors = Introspector.getBeanInfo(geometry.getClass())
        .getPropertyDescriptors();

    Map<Class<?>, Set<PropertyDescriptor>> classPropertiesMap = new HashMap<>();

    Map<Class<?>, Set<PropertyDescriptor>> map = Arrays.stream(descriptors)
        .collect(Collectors.groupingBy(descriptor -> ReflectedPropertySectionBuilder
            .getPropertyDescriptorClassReference(descriptor), Collectors.toSet()));

    for (PropertyDescriptor descriptor : descriptors) {
      Class<?> propertyRefClass = ReflectedPropertySectionBuilder
          .getPropertyDescriptorClassReference(descriptor);
      Set<PropertyDescriptor> propertyDescriptorSet = classPropertiesMap.get(propertyRefClass);
      if (propertyDescriptorSet == null) {
        propertyDescriptorSet = new HashSet<>();
        classPropertiesMap.put(propertyRefClass, propertyDescriptorSet);
      }
      propertyDescriptorSet.add(descriptor);
    }
    Assertions.assertEquals(classPropertiesMap, map);
  }

}