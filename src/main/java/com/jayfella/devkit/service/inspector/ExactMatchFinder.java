package com.jayfella.devkit.service.inspector;

import com.jayfella.devkit.properties.PropertySection;
import com.jayfella.devkit.properties.builder.AbstractPropertySectionBuilder;
import com.jayfella.devkit.properties.builder.PropertySectionBuilderFactory;
import com.jayfella.devkit.service.RegistrationService;
import com.jayfella.devkit.service.ServiceManager;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExactMatchFinder extends PropertySectionListBuilder {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExactMatchFinder.class);

  @Override
  public List<PropertySection> find(Object object) {
    RegistrationService registrationService = ServiceManager
        .getService(RegistrationService.class);
    PropertySectionBuilderFactory factory = registrationService
        .getPropertySectionBuilderFactoryFor(object.getClass());
    if (factory != null) {
      AbstractPropertySectionBuilder<?> builder = factory.create(object);
      return builder.build();
    }
    return findNext(object);
  }

}
