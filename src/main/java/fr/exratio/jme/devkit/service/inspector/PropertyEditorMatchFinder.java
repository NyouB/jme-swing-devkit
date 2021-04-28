package fr.exratio.jme.devkit.service.inspector;

import fr.exratio.jme.devkit.properties.PropertySection;
import fr.exratio.jme.devkit.properties.builder.ReflectedPropertySectionBuilder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyEditorMatchFinder extends PropertySectionListFinder {

  private static final Logger LOGGER = LoggerFactory.getLogger(PropertyEditorMatchFinder.class);


  @Override
  public List<PropertySection> find(Object object) {
    // we don't know what it is, so all we can do is display reflected properties.
    ReflectedPropertySectionBuilder componentSetBuilder = new ReflectedPropertySectionBuilder(
        object, exactMatchFinder);
    return componentSetBuilder.build();
  }

}
