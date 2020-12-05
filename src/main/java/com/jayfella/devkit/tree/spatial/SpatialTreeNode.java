package com.jayfella.devkit.tree.spatial;

import com.jayfella.devkit.tree.JmeTreeNode;
import com.jme3.scene.Spatial;

public abstract class SpatialTreeNode extends JmeTreeNode {

  public SpatialTreeNode(Spatial spatial) {
    super(spatial);
  }

  @Override
  public Spatial getUserObject() {
    return (Spatial) super.getUserObject();
  }

}
