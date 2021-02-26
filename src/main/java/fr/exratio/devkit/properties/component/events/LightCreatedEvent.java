package fr.exratio.devkit.properties.component.events;

import com.jme3.light.Light;
import fr.exratio.devkit.event.Event;

public class LightCreatedEvent extends Event {

  private final Light light;

  public LightCreatedEvent(Light light) {
    this.light = light;
  }

  public Light getLight() {
    return light;
  }

}
