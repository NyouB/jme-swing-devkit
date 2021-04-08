package fr.exratio.jme.devkit.service.inspector;

import fr.exratio.jme.devkit.properties.PropertySection;
import fr.exratio.jme.devkit.properties.builder.AbstractPropertySectionBuilder;
import fr.exratio.jme.devkit.service.RegistrationService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ExactMatchFinder extends PropertySectionListFinder {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExactMatchFinder.class);
  private final RegistrationService registrationService;

  @Autowired
  public ExactMatchFinder(RegistrationService registrationService){
    this.registrationService = registrationService;
  }

  @Override
  public List<PropertySection> find(Object object) {
    AbstractPropertySectionBuilder propertySectionBuilder = registrationService
        .getPropertySectionBuilderInstance(object.getClass(), object);
    if (propertySectionBuilder != null) {
      LOGGER.debug("-- find() Factory {} found for class {}",
          propertySectionBuilder.getClass().getCanonicalName(),
          object.getClass().getCanonicalName());
      propertySectionBuilder.withObject(object);
      return propertySectionBuilder.build();
    }
    return findNext(object);
  }

}
