package com.jayfella.devkit.properties.component.control;

import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jayfella.devkit.properties.component.SDKComponentFactory;
import com.jme3.animation.AnimControl;

public class AnimControlComponentFactory implements SDKComponentFactory<AnimControl> {

  @Override
  public AbstractSDKComponent<AnimControl> create(AnimControl object) {
    return new AnimControlComponent(object);
  }
}
