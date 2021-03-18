package fr.exratio.jme.devkit.event;

import com.jme3.scene.Spatial;

public class SpatialCreatedEvent {

  private final Spatial spatial;

  public SpatialCreatedEvent(Spatial spatial) {
    this.spatial = spatial;
  }

  public Spatial getSpatial() {
    return spatial;
  }

}
