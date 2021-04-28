package fr.exratio.jme.devkit.registration.spatial;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import com.jme3.scene.instancing.InstancedNode;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.SceneGraphService;
import fr.exratio.jme.devkit.tree.spatial.NodeTreeNode;
import java.awt.HeadlessException;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Service
public class InstancedNodeSpatialRegistrar extends NodeRegistrar {

  @Autowired
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

    public InstancedNodeTreeNode(InstancedNode instancedNode,
        InstancedNodeContextMenu instancedNodeContextMenu) {
      super(instancedNode, instancedNodeContextMenu);
    }

    @Override
    public InstancedNode getUserObject() {
      return (InstancedNode) super.getUserObject();
    }

  }

  @Controller
  public static class InstancedNodeContextMenu extends JPopupMenu {

    private final EditorJmeApplication editorJmeApplication;
    private final SceneGraphService sceneGraphService;

    @Autowired
    public InstancedNodeContextMenu(EditorJmeApplication editorJmeApplication,
        SceneGraphService sceneGraphService)
        throws HeadlessException {
      this.editorJmeApplication = editorJmeApplication;
      this.sceneGraphService = sceneGraphService;

      JMenuItem instanceItem = add(new JMenuItem("Instance Items"));
      instanceItem.addActionListener(e -> editorJmeApplication.enqueue(() -> {

        try {
          ((InstancedNodeTreeNode) sceneGraphService.getSelectedObject()).getUserObject()
              .instance();
        } catch (IllegalStateException ex) {

          JOptionPane.showMessageDialog(null,
              ex.getMessage(),
              "Instancing Error",
              JOptionPane.ERROR_MESSAGE);

        }

      }));

    }

  }

}
