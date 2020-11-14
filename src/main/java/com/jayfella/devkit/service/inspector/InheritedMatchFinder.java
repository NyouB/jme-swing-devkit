package com.jayfella.devkit.service.inspector;

import com.jayfella.devkit.properties.PropertySection;
import com.jayfella.devkit.properties.builder.AbstractPropertySectionBuilder;
import com.jayfella.devkit.properties.builder.PropertySectionBuilderFactory;
import com.jayfella.devkit.service.RegistrationService;
import com.jayfella.devkit.service.ServiceManager;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InheritedMatchFinder extends PropertySectionListBuilder {

  private static final Logger LOGGER = LoggerFactory.getLogger(InheritedMatchFinder.class);

  @Override
  public List<PropertySection> find(Object object) {
    PropertySectionBuilderFactory factory = findParentClassFactory(object);
    if (factory != null) {
      LOGGER.debug("-- find() Factory {} found for class {}", factory.getClass().getCanonicalName(), object.getClass().getCanonicalName());
      AbstractPropertySectionBuilder<?> builder = factory.create(object);
      return builder.build();
    }
    return findNext(object);
  }

  public PropertySectionBuilderFactory findParentClassFactory(Object object) {
    RegistrationService registrationService = ServiceManager
        .getService(RegistrationService.class);
    Set<Class<?>> keySet = registrationService.getPropertySectionBuilderFactoryMap()
        .keySet();
    for (Class clazz : keySet) {
      if (clazz.isInstance(object)) {
        LOGGER.warn(
            "-- inspect() A parent class's factory {} has been found for class {} instead of exact one. Some fields representation may miss",
            clazz.getCanonicalName(), object.getClass().getCanonicalName());
        return registrationService
            .getPropertySectionBuilderFactoryFor(clazz);
      }
    }
    return null;
  }

}
