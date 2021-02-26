package fr.exratio.devkit.properties.component.events;

import com.jme3.scene.Spatial;
import fr.exratio.devkit.event.Event;

public class SpatialRemovedEvent extends Event {

  private final Spatial spatial;

  public SpatialRemovedEvent(Spatial spatial) {
    this.spatial = spatial;
  }

  public Spatial getSpatial() {
    return spatial;
  }

}
