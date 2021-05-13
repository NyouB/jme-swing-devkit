package fr.exratio.jme.devkit.service.inspector;

import fr.exratio.jme.devkit.properties.PropertySection;
import fr.exratio.jme.devkit.properties.builder.AbstractPropertySectionBuilder;
import fr.exratio.jme.devkit.service.RegistrationService;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InheritedMatchFinder extends PropertySectionListFinder {

  private static final Logger LOGGER = LoggerFactory.getLogger(InheritedMatchFinder.class);

  private final RegistrationService registrationService;

  @Autowired
  public InheritedMatchFinder(RegistrationService registrationService) {
    this.registrationService = registrationService;
  }

  @Override
  public List<PropertySection> find(Object object) {

    Class<?> registeredParentClass = findRegisteredParentClass(
        object);
    AbstractPropertySectionBuilder propertySectionBuilder = registrationService
        .getPropertySectionBuilder(registeredParentClass);
    if (propertySectionBuilder != null) {
      LOGGER.debug("-- find() builder found for parent class {}, initial object class {}",
          registeredParentClass.getCanonicalName(), object.getClass().getCanonicalName());
      return propertySectionBuilder.withObject(object).build();
    }
    LOGGER.debug("-- find() No builder found for class {}",
        object.getClass().getCanonicalName());
    return findNext(object);
  }

  public Class<?> findRegisteredParentClass(
      Object object) {
    Set<Class<?>> keySet = registrationService.getRegisteredClasses();
    return findClosestRegisteredParentClass(object.getClass(), keySet);
  }

  public Class findClosestRegisteredParentClass(Class clazz, Set<Class<?>> keySet) {
    if (Object.class == clazz) {
      return null;
    }
    if (keySet.contains(clazz)) {
      LOGGER.warn(
          "-- findClosestRegisteredParentClass() A registered parent class {} has been found instead of exact one. Some fields representation may miss",
          clazz.getCanonicalName());
      return clazz;
    }
    return findClosestRegisteredParentClass(clazz.getSuperclass(), keySet);
  }

}
