package fr.exratio.jme.devkit.event;

import com.jme3.material.Material;
import lombok.Data;

@Data
public class MaterialUpdateEvent {

  private final Material material;

  public MaterialUpdateEvent(Material newValue) {
    this.material = newValue;
  }
}
