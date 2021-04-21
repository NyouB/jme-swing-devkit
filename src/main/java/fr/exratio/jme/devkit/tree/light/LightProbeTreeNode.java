package fr.exratio.jme.devkit.tree.light;

import com.jme3.light.LightProbe;

public class LightProbeTreeNode extends LightTreeNode {

  public LightProbeTreeNode(LightProbe light) {
    super(light);
  }

  @Override
  public LightProbe getUserObject() {
    return (LightProbe) super.getUserObject();
  }

}
