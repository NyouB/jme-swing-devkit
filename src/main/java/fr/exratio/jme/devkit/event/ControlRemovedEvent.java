package fr.exratio.jme.devkit.event;

import com.jme3.scene.control.Control;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ControlRemovedEvent {

  private final Control control;

  public ControlRemovedEvent(Control control) {
    this.control = control;
  }

  public Control getControl() {
    return control;
  }

}
