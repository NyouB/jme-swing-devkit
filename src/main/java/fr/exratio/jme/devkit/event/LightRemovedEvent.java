package fr.exratio.jme.devkit.event;

import com.jme3.light.Light;

public class LightRemovedEvent{

  private final Light light;

  public LightRemovedEvent(Light light) {
    this.light = light;
  }

  public Light getLight() {
    return light;
  }

}
