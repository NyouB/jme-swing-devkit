package fr.exratio.jme.devkit.tree.light;

import com.jme3.light.DirectionalLight;

public class DirectionalLightTreeNode extends LightTreeNode {

  public DirectionalLightTreeNode(DirectionalLight light) {
    super(light);
  }

  @Override
  public DirectionalLight getUserObject() {
    return (DirectionalLight) super.getUserObject();
  }

}
