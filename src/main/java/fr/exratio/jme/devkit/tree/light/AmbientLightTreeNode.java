package fr.exratio.jme.devkit.tree.light;

import com.jme3.light.AmbientLight;

public class AmbientLightTreeNode extends LightTreeNode {

  public AmbientLightTreeNode(AmbientLight ambientLight) {
    super(ambientLight);
  }

  @Override
  public AmbientLight getUserObject() {
    return (AmbientLight) super.getUserObject();
  }

}
