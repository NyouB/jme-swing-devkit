package fr.exratio.devkit.service.inspector;

import fr.exratio.devkit.properties.PropertySection;
import fr.exratio.devkit.properties.builder.AbstractPropertySectionBuilder;
import fr.exratio.devkit.service.RegistrationService;
import fr.exratio.devkit.service.ServiceManager;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InheritedMatchFinder extends PropertySectionListFinder {

  private static final Logger LOGGER = LoggerFactory.getLogger(InheritedMatchFinder.class);

  @Override
  public List<PropertySection> find(Object object) {

    Class<?> registeredParentClass = findRegisteredParentClass(
        object);
    RegistrationService registrationService = ServiceManager
        .getService(RegistrationService.class);
    Class<? extends AbstractPropertySectionBuilder> sectionBuilderClass = registrationService
        .getPropertySectionBuilder(registeredParentClass);
    if (sectionBuilderClass != null) {
      LOGGER.debug("-- find() builder found for parent class {}, initial object class {}",
          registeredParentClass.getCanonicalName(), object.getClass().getCanonicalName());
      try {
        AbstractPropertySectionBuilder<?> builder = sectionBuilderClass
            .getConstructor(registeredParentClass)
            .newInstance(object);
        return builder.build();
      } catch (Exception e) {
        LOGGER.warn("-- find() Error while instanciating builder {}",
            sectionBuilderClass.getSimpleName(), e);
      }
    }
    LOGGER.debug("-- find() No builder found for class {}",
        object.getClass().getCanonicalName());
    return findNext(object);
  }

  public Class<?> findRegisteredParentClass(
      Object object) {
    RegistrationService registrationService = ServiceManager
        .getService(RegistrationService.class);
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
