package fr.exratio.jme.devkit.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatchEvent {

  private Object node;

  public BatchEvent(Object node) {
    this.node = node;
  }

  public Object getControl() {
    return node;
  }

}
