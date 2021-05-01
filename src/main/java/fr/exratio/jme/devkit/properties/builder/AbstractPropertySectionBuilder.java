package fr.exratio.jme.devkit.properties.builder;

import fr.exratio.jme.devkit.properties.PropertySection;
import java.lang.reflect.Field;
import java.util.List;
import org.apache.commons.lang3.builder.Builder;

public abstract class AbstractPropertySectionBuilder<T> implements Builder<List<PropertySection>> {

  protected T object;

  protected List<Field> ignoredFields;

  public AbstractPropertySectionBuilder() {
  }

  public AbstractPropertySectionBuilder<T> withObject(T object) {
    this.object = object;
    return this;
  }

  @Override
  public abstract List<PropertySection> build();


}
