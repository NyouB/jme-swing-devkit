package com.jayfella.devkit.properties.builder;

import com.jayfella.devkit.properties.PropertySection;
import com.jayfella.devkit.properties.component.AbstractJmeDevKitTest;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import java.awt.Component;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ReflectedPropertySectionBuilderTest extends AbstractJmeDevKitTest {

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


  @Test
  void build() {
    Mesh myTestMesh = new Mesh();
    Geometry myTestGeometry = new Geometry("testGeometry", myTestMesh);
    ReflectedPropertySectionBuilder reflectedPropertySectionBuilder = new ReflectedPropertySectionBuilder(
        myTestGeometry);
    List<PropertySection> propertySectionsResult = reflectedPropertySectionBuilder
        .build();
    Assertions.assertNotNull(propertySectionsResult);
    for (PropertySection propertySection : propertySectionsResult) {
      Map<String, Component> components = propertySection.getComponents();
      for (Entry<String, Component> entry : components.entrySet()) {
        System.out.println(entry.getKey());
        if (entry.getValue() == null) {
          System.out.println("null");
        }
        Assertions.assertNotNull(entry.getValue());
      }
    }

  }

}