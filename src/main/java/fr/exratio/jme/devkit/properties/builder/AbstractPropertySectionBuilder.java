package fr.exratio.jme.devkit.properties.builder;

import fr.exratio.jme.devkit.properties.PropertySection;
import fr.exratio.jme.devkit.properties.component.AbstractPropertyEditor;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.ServiceManager;
import java.lang.reflect.Field;
import java.util.List;
import org.apache.commons.lang3.builder.Builder;

public abstract class AbstractPropertySectionBuilder<T> implements Builder<List<PropertySection>> {

  protected T object;
  protected List<Field> ignoredFields;

  public AbstractPropertySectionBuilder<T> withObject(T object){
    this.object = object;
    return this;
  }

}
