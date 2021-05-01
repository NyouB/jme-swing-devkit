package fr.exratio.jme.devkit.tree.spatial;

import com.jme3.scene.Node;

public class NodeTreeNode extends SpatialTreeNode {

  public NodeTreeNode(Node node) {
    super(node);
  }

  @Override
  public Node getUserObject() {
    return (Node) super.getUserObject();
  }


}
