package com.jayfella.devkit.tree.light;

import com.jayfella.devkit.tree.TreeContextMenu;
import com.jme3.light.Light;
import javax.swing.tree.DefaultMutableTreeNode;

public abstract class LightTreeNode extends DefaultMutableTreeNode implements TreeContextMenu {

  public LightTreeNode(Light light) {
    super(light);
  }

  @Override
  public Light getUserObject() {
    return (Light) super.getUserObject();
  }

}
