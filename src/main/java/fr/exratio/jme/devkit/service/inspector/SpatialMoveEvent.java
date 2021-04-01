package fr.exratio.jme.devkit.service.inspector;

import com.jme3.scene.Spatial;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SpatialMoveEvent {

  private Spatial spatialItem;
}
