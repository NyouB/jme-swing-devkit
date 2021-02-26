package fr.exratio.devkit.registration.spatial;

import com.jme3.scene.Node;
import fr.exratio.devkit.registration.AbstractClassRegistrar;

public abstract class NodeRegistrar extends AbstractClassRegistrar<Node> {

  public NodeRegistrar(Class<? extends Node> clazz) {
    setRegisteredClass(clazz);
  }
}
