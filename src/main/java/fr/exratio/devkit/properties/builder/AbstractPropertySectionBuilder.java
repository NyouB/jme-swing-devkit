package fr.exratio.devkit.properties.builder;

import fr.exratio.devkit.properties.PropertySection;
import fr.exratio.devkit.properties.component.AbstractPropertyEditor;
import fr.exratio.devkit.service.JmeEngineService;
import fr.exratio.devkit.service.ServiceManager;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.Builder;

public abstract class AbstractPropertySectionBuilder<T> implements Builder<List<PropertySection>> {

  protected final T object;
  protected final List<Field> ignoredFields;

  public AbstractPropertySectionBuilder(T object, Field... ignoredFieldsArray) {
    this.object = object;
    this.ignoredFields = Arrays.asList(ignoredFieldsArray.clone());
  }

  public void bind(Field field, AbstractPropertyEditor component) {
    component.addPropertyChangeListener(
        value -> ServiceManager.getService(JmeEngineService.class).enqueue(() -> {
          try {
            field.set(object, value);
          } catch (IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
          }
        }));
  }


}
