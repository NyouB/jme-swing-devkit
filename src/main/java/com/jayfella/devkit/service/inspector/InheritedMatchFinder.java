package com.jayfella.devkit.service.inspector;

import com.jayfella.devkit.properties.PropertySection;
import com.jayfella.devkit.properties.builder.AbstractPropertySectionBuilder;
import com.jayfella.devkit.service.RegistrationService;
import com.jayfella.devkit.service.ServiceManager;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InheritedMatchFinder extends PropertySectionListFinder {

  private static final Logger LOGGER = LoggerFactory.getLogger(InheritedMatchFinder.class);

  @Override
  public List<PropertySection> find(Object object, String propertyName) {
    Class<? extends AbstractPropertySectionBuilder> sectionBuilderClass = findSectionBuilderForParentClass(
        object);
    if (sectionBuilderClass != null) {
      LOGGER.debug("-- find() No builder found for class {}",
          object.getClass().getCanonicalName());
      try {
        AbstractPropertySectionBuilder<?> builder = sectionBuilderClass.getConstructor()
            .newInstance(object);
        return builder.build();
      } catch (Exception e) {
        LOGGER.warn("-- find() Error while instanciating builder {}",
            sectionBuilderClass.getSimpleName(), e);
      }

    }
    return findNext(object, propertyName);
  }

  public Class<? extends AbstractPropertySectionBuilder> findSectionBuilderForParentClass(
      Object object) {
    RegistrationService registrationService = ServiceManager
        .getService(RegistrationService.class);
    Set<Class<?>> keySet = registrationService.getRegisteredClasses();
    for (Class clazz : keySet) {
      if (clazz.isInstance(object)) {
        LOGGER.warn(
            "-- findSectionBuilderForParentClass() A parent class's factory {} has been found for class {} instead of exact one. Some fields representation may miss",
            clazz.getCanonicalName(), object.getClass().getCanonicalName());
        return registrationService.getPropertySectionBuilder(clazz);
      }
    }
    return null;
  }

}
