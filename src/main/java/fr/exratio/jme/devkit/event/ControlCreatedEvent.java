package fr.exratio.jme.devkit.event;

import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ControlCreatedEvent {

  private Spatial parent;

  private final Control control;

  public ControlCreatedEvent(Control control) {
    this.control = control;
  }

  public Control getControl() {
    return control;
  }

}
