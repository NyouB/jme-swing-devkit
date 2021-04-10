package fr.exratio.jme.devkit.properties.builder;

import fr.exratio.jme.devkit.properties.PropertySection;
import fr.exratio.jme.devkit.properties.component.AbstractPropertyEditor;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.ServiceManager;
import java.lang.reflect.Field;
import java.util.List;
import org.apache.commons.lang3.builder.Builder;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractPropertySectionBuilder<G> implements Builder<List<PropertySection>> {

  protected G object;
  protected List<Field> ignoredFields;

  public AbstractPropertySectionBuilder<G> withObject(G object){
    this.object = object;
    return this;
  }

  @Override
  public abstract List<PropertySection> build();


}
