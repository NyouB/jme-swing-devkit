package com.jayfella.devkit.properties.component.control;

import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jayfella.devkit.properties.component.SDKComponentFactory;
import com.jme3.anim.AnimComposer;

public class AnimComposerComponentFactory implements SDKComponentFactory<AnimComposer> {

  @Override
  public AbstractSDKComponent<AnimComposer> create(AnimComposer object, String propertyName) {
    return new AnimComposerComponent(object);
  }
}
