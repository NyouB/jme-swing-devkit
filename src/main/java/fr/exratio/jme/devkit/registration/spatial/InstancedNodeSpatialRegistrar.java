package fr.exratio.jme.devkit.registration.spatial;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import com.jme3.scene.instancing.InstancedNode;
import fr.exratio.jme.devkit.service.JmeEngineService;
import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.tree.spatial.NodeTreeNode;
import fr.exratio.jme.devkit.tree.spatial.menu.NodeContextMenu;
import java.awt.HeadlessException;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreeNode;

public class InstancedNodeSpatialRegistrar extends NodeRegistrar {

  public InstancedNodeSpatialRegistrar() {
    super(InstancedNode.class);
  }

  @Override
  public Node createInstance(SimpleApplication application) {
    return new InstancedNode("New InstancedNode");
  }

  @Override
  public TreeNode createSceneTreeNode(Node node, SimpleApplication application) {
    return null;
  }

  public static class InstancedNodeTreeNode extends NodeTreeNode {

    public InstancedNodeTreeNode(InstancedNode instancedNode) {
      super(instancedNode);
    }

    @Override
    public InstancedNode getUserObject() {
      return (InstancedNode) super.getUserObject();
    }

    @Override
    public JPopupMenu getContextMenu() {
      return new InstancedNodeContextMenu(this);
    }
  }

  public static class InstancedNodeContextMenu extends NodeContextMenu {

    public InstancedNodeContextMenu(InstancedNodeTreeNode instancedNodeTreeNode)
        throws HeadlessException {
      super(instancedNodeTreeNode);

      JMenuItem instanceItem = add(new JMenuItem("Instance Items"));
      instanceItem.addActionListener(e -> {
        ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

          try {
            instancedNodeTreeNode.getUserObject().instance();
          } catch (IllegalStateException ex) {

            JOptionPane.showMessageDialog(null,
                ex.getMessage(),
                "Instancing Error",
                JOptionPane.ERROR_MESSAGE);

          }

        });
      });

    }

  }

}
