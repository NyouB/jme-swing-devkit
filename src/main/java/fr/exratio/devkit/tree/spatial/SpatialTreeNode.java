package fr.exratio.devkit.tree.spatial;

import com.jme3.scene.Spatial;
import fr.exratio.devkit.tree.JmeTreeNode;

public abstract class SpatialTreeNode extends JmeTreeNode {

  public SpatialTreeNode(Spatial spatial) {
    super(spatial);
  }

  @Override
  public Spatial getUserObject() {
    return (Spatial) super.getUserObject();
  }

}
