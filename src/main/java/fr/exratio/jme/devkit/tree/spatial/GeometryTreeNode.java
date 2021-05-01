package fr.exratio.jme.devkit.tree.spatial;

import com.jme3.scene.Geometry;

public class GeometryTreeNode extends SpatialTreeNode {

  public GeometryTreeNode(Geometry geometry) {
    super(geometry);
  }

  @Override
  public Geometry getUserObject() {
    return (Geometry) super.getUserObject();
  }

}
