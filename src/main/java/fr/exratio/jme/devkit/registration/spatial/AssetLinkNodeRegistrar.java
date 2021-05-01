package fr.exratio.jme.devkit.registration.spatial;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.AssetLinkNode;
import com.jme3.scene.Node;
import fr.exratio.jme.devkit.tree.spatial.NodeTreeNode;
import javax.swing.tree.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetLinkNodeRegistrar extends NodeRegistrar {

  @Autowired
  public AssetLinkNodeRegistrar() {
    super(AssetLinkNode.class);
  }

  @Override
  public Node createInstance(SimpleApplication application) {
    return new AssetLinkNode();
  }

  @Override
  public TreeNode createSceneTreeNode(Node instance, SimpleApplication application) {
    return new AssetLinkNodeTreeNode(instance);
  }

  public static class AssetLinkNodeTreeNode extends NodeTreeNode {

    public AssetLinkNodeTreeNode(Node node) {
      super(node);
    }

    @Override
    public AssetLinkNode getUserObject() {
      return (AssetLinkNode) super.getUserObject();
    }

  }


}
