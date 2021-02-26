package fr.exratio.devkit.service.inspector;

import fr.exratio.devkit.properties.PropertySection;
import java.util.ArrayList;
import java.util.List;

public abstract class PropertySectionListFinder {

  private PropertySectionListFinder next;


  public PropertySectionListFinder chainWith(PropertySectionListFinder builder) {
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
