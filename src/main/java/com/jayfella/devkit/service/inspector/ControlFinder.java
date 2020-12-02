package com.jayfella.devkit.service.inspector;

import com.jayfella.devkit.properties.PropertySection;
import com.jayfella.devkit.properties.builder.AbstractPropertySectionBuilder;
import com.jayfella.devkit.properties.component.AbstractPropertyEditor;
import com.jayfella.devkit.properties.component.SDKComponentFactory;
import com.jayfella.devkit.service.RegistrationService;
import com.jayfella.devkit.service.ServiceManager;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControlFinder extends PropertySectionListBuilder {

  private static final Logger LOGGER = LoggerFactory.getLogger(ControlFinder.class);


  @Override
  public List<PropertySection> find(Class<?> clazz, Object object, String propertyName) {
    Class<? extends AbstractPropertySectionBuilder> editorClass = ServiceManager
        .getService(RegistrationService.class)
        .getPropertySectionBuilder(object.getClass());
    try {
      AbstractPropertySectionBuilder editor = editorClass.getConstructor(editorClass).newInstance(object);
      if (editor != null) {
        LOGGER.debug("-- find() Editor {} found for class {}", editorClass.getCanonicalName(),
            object.getClass().getCanonicalName());
        return (List<PropertySection>) editor.build();
      }
    
    } catch (Exception e) {
      LOGGER.debug("-- find() No editor found for class {}, tryinh next strategy", clazz.getSimpleName());
    }
    
    return findNext(clazz, object, propertyName);
  }

}
