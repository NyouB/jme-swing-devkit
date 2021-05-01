package fr.exratio.jme.devkit.registration.spatial;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.BatchNode;
import com.jme3.scene.Node;
import fr.exratio.jme.devkit.tree.spatial.NodeTreeNode;
import javax.swing.tree.TreeNode;

public class BatchNodeRegistrar extends NodeRegistrar {

  public BatchNodeRegistrar() {
    super(BatchNode.class);
  }

  @Override
  public Node createInstance(SimpleApplication application) {
    return new BatchNode();
  }

  @Override
  public TreeNode createSceneTreeNode(Node instance, SimpleApplication application) {
    return null;
  }

  public static class BatchNodeTreeNode extends NodeTreeNode {

    public BatchNodeTreeNode(BatchNode node) {
      super(node);
    }

    @Override
    public BatchNode getUserObject() {
      return (BatchNode) super.getUserObject();
    }


  }

}
