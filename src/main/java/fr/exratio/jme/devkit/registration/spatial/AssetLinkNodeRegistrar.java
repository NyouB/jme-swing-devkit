package fr.exratio.jme.devkit.registration.spatial;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.AssetLinkNode;
import com.jme3.scene.Node;
import fr.exratio.jme.devkit.tree.spatial.NodeTreeNode;
import fr.exratio.jme.devkit.tree.spatial.menu.SpatialContextMenu;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetLinkNodeRegistrar extends NodeRegistrar {

  private final SpatialContextMenu nodeContextMenu;

  @Autowired
  public AssetLinkNodeRegistrar(
      SpatialContextMenu nodeContextMenu) {
    super(AssetLinkNode.class);
    this.nodeContextMenu = nodeContextMenu;
  }

  @Override
  public Node createInstance(SimpleApplication application) {
    return new AssetLinkNode();
  }

  @Override
  public TreeNode createSceneTreeNode(Node instance, SimpleApplication application) {
    return new AssetLinkNodeTreeNode(instance, nodeContextMenu);
  }

  public static class AssetLinkNodeTreeNode extends NodeTreeNode {

    private final SpatialContextMenu nodeContextMenu;

    public AssetLinkNodeTreeNode(Node node,
        SpatialContextMenu nodeContextMenu) {
      super(node, nodeContextMenu);
      this.nodeContextMenu = nodeContextMenu;
    }

    @Override
    public AssetLinkNode getUserObject() {
      return (AssetLinkNode) super.getUserObject();
    }

    @Override
    public JPopupMenu getContextMenu() {
      return nodeContextMenu;
    }
  }


}
