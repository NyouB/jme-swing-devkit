package fr.exratio.jme.devkit.properties.component.events;

import com.jme3.scene.Spatial;
import fr.exratio.jme.devkit.event.Event;

public class SpatialNameChangedEvent extends Event {

  private final Spatial spatial;

  public SpatialNameChangedEvent(Spatial spatial) {
    this.spatial = spatial;
  }

  public Spatial getSpatial() {
    return spatial;
  }

}
