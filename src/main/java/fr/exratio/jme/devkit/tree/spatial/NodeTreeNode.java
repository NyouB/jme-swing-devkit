package fr.exratio.jme.devkit.tree.spatial;

import com.jme3.scene.Node;
import javax.swing.JPopupMenu;

public class NodeTreeNode extends SpatialTreeNode {

  private final JPopupMenu nodeContextMenu;

  public NodeTreeNode(Node node,
      JPopupMenu nodeContextMenu) {
    super(node);
    this.nodeContextMenu = nodeContextMenu;
  }

  @Override
  public Node getUserObject() {
    return (Node) super.getUserObject();
  }

  @Override
  public JPopupMenu getContextMenu() {
    return nodeContextMenu;
  }

}
