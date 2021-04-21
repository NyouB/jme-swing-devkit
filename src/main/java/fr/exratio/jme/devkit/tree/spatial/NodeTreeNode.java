package fr.exratio.jme.devkit.tree.spatial;

import com.jme3.scene.Node;
import fr.exratio.jme.devkit.tree.spatial.menu.NodeContextMenu;
import javax.swing.JPopupMenu;

public class NodeTreeNode extends SpatialTreeNode {

  private final NodeContextMenu nodeContextMenu;

  public NodeTreeNode(Node node,
      NodeContextMenu nodeContextMenu) {
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
