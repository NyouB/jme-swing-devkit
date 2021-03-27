package fr.exratio.jme.devkit.event;

import com.jme3.scene.Spatial;

public class SpatialNameChangedEvent {

  private final Spatial spatial;

  public SpatialNameChangedEvent(Spatial spatial) {
    this.spatial = spatial;
  }

  public Spatial getSpatial() {
    return spatial;
  }

}
