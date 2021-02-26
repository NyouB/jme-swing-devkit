package fr.exratio.devkit.properties.component.events;

import com.jme3.scene.control.Control;
import fr.exratio.devkit.event.Event;

public class ControlRemovedEvent extends Event {

  private final Control control;

  public ControlRemovedEvent(Control control) {
    this.control = control;
  }

  public Control getControl() {
    return control;
  }

}
