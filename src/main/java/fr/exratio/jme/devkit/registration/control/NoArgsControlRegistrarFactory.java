package fr.exratio.jme.devkit.registration.control;

import com.jme3.scene.control.Control;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NoArgsControlRegistrarFactory {

  @Autowired
  public NoArgsControlRegistrarFactory() {
  }

  public NoArgsControlRegistrar create(Class<? extends Control> controlClass) {
    NoArgsControlRegistrar registrar = new NoArgsControlRegistrar();
    registrar.setRegisteredClass(controlClass);
    return registrar;
  }
}
