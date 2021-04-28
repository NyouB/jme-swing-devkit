package fr.exratio.jme.devkit.registration.control;

import com.jme3.scene.control.Control;
import fr.exratio.jme.devkit.tree.control.ControlContextMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NoArgsControlRegistrarFactory {

  private final ControlContextMenu controlContextMenu;

  @Autowired
  public NoArgsControlRegistrarFactory(
      ControlContextMenu controlContextMenu) {
    this.controlContextMenu = controlContextMenu;
  }

  public NoArgsControlRegistrar create(Class<? extends Control> controlClass) {
    NoArgsControlRegistrar registrar = new NoArgsControlRegistrar(controlContextMenu);
    registrar.setRegisteredClass(controlClass);
    return registrar;
  }
}
