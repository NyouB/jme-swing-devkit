package com.jayfella.devkit.service.inspector;

import com.jayfella.devkit.properties.PropertySection;
import com.jayfella.devkit.properties.component.AbstractPropertyEditor;
import com.jayfella.devkit.properties.component.SDKComponentFactory;
import com.jayfella.devkit.service.RegistrationService;
import com.jayfella.devkit.service.ServiceManager;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControlFinder extends PropertySectionListBuilder {

  private static final Logger LOGGER = LoggerFactory.getLogger(ControlFinder.class);


  @Override
  public List<PropertySection> find(Class<?> clazz, Object object, String propertyName) {
    SDKComponentFactory factory = ServiceManager
        .getService(RegistrationService.class)
        .getComponentFactoryFor(object.getClass());
    if (factory != null) {
      LOGGER.debug("-- find() Factory {} found for class {}", factory.getClass().getCanonicalName(),
          object.getClass().getCanonicalName());
      AbstractPropertyEditor component = factory.create(object, propertyName);
      PropertySection propertySection = new PropertySection(propertyName,
          component);
      List<PropertySection> propertySectionList = new ArrayList<>();
      propertySectionList.add(propertySection);
      return propertySectionList;
    }
    return findNext(clazz, object, propertyName);
  }

}
