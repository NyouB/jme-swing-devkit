package com.jayfella.devkit.properties.component.events;

import com.jayfella.devkit.event.Event;
import com.jme3.scene.control.Control;

public class ControlRemovedEvent extends Event {

  private final Control control;

  public ControlRemovedEvent(Control control) {
    this.control = control;
  }

  public Control getControl() {
    return control;
  }

}
