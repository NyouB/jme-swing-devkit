package fr.exratio.jme.devkit.registration.spatial;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.BatchNode;
import com.jme3.scene.Node;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.SceneTreeService;
import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.tree.spatial.NodeTreeNode;
import fr.exratio.jme.devkit.tree.spatial.menu.NodeContextMenu;
import java.awt.HeadlessException;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
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

    @Override
    public JPopupMenu getContextMenu() {
      return new BatchNodeContextMenu(this);
    }

  }

  public static class BatchNodeContextMenu extends NodeContextMenu {

    public BatchNodeContextMenu(BatchNodeTreeNode nodeTreeNode) throws HeadlessException {
      super(nodeTreeNode, createCylinderAction, createDomeAction, createQuadAction,
          createSphereAction);

      add(new JSeparator());

      JMenuItem batchItem = add(new JMenuItem("Batch"));
      batchItem
          .addActionListener(e -> ServiceManager.getService(EditorJmeApplication.class).enqueue(() -> {
            nodeTreeNode.getUserObject().batch();
            ServiceManager.getService(SceneTreeService.class).reloadTreeNode(nodeTreeNode);
          }));

    }

  }

}
