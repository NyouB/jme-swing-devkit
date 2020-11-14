package com.jayfella.devkit.service.inspector;

import com.jayfella.devkit.properties.PropertySection;
import java.util.ArrayList;
import java.util.List;

public abstract class PropertySectionListBuilder {

  private PropertySectionListBuilder next;


  public PropertySectionListBuilder chainWith(PropertySectionListBuilder builder) {
    this.next = builder;
    return next;
  }

  /**
   * Runs check on the next object in chain or ends traversing if we're in last object in chain.
   */
  protected List<PropertySection> findNext(Object object) {
    if (next == null) {
      return new ArrayList<>();
    }
    return next.find(object);
  }

  public abstract List<PropertySection> find(Object object);

}
