package fr.exratio.devkit.tree.spatial;

import com.jme3.scene.Node;
import fr.exratio.devkit.tree.spatial.menu.NodeContextMenu;
import javax.swing.JPopupMenu;

public class NodeTreeNode extends SpatialTreeNode {

  public NodeTreeNode(Node node) {
    super(node);

  }

  @Override
  public Node getUserObject() {
    return (Node) super.getUserObject();
  }

  @Override
  public JPopupMenu getContextMenu() {
    return new NodeContextMenu(this);
  }

}
