package fr.exratio.jme.devkit.tree.light;

import com.jme3.light.PointLight;

public class PointLightTreeNode extends LightTreeNode {

  public PointLightTreeNode(PointLight light) {
    super(light);
  }

  @Override
  public PointLight getUserObject() {
    return (PointLight) super.getUserObject();
  }

}
