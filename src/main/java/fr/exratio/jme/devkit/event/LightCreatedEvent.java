package fr.exratio.jme.devkit.event;

import com.jme3.light.Light;
import com.jme3.scene.Spatial;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LightCreatedEvent {


  private final Spatial parent;
  private final Light light;

  public LightCreatedEvent(Spatial parent, Light light) {
    this.parent = parent;
    this.light = light;
  }

  public Light getLight() {
    return light;
  }

}
