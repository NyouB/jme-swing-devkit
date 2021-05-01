package fr.exratio.jme.devkit.service.inspector;

import fr.exratio.jme.devkit.properties.PropertySection;
import fr.exratio.jme.devkit.properties.builder.ReflectedPropertySectionBuilder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultMatchFinder extends PropertySectionListFinder {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMatchFinder.class);

  private final ReflectedPropertySectionBuilder reflectedPropertySectionBuilder;

  public DefaultMatchFinder(
      ReflectedPropertySectionBuilder reflectedPropertySectionBuilder) {
    this.reflectedPropertySectionBuilder = reflectedPropertySectionBuilder;
  }


  @Override
  public List<PropertySection> find(Object object) {
    // we don't know what it is, so all we can do is display reflected properties.
    return reflectedPropertySectionBuilder.withObject(object).build();
  }

}
