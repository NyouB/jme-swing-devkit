package fr.exratio.jme.devkit.properties.component.events;

import com.jme3.light.Light;
import fr.exratio.jme.devkit.event.Event;

public class LightRemovedEvent extends Event {

  private final Light light;

  public LightRemovedEvent(Light light) {
    this.light = light;
  }

  public Light getLight() {
    return light;
  }

}
