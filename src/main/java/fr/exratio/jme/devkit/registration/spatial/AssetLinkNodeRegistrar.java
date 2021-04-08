package fr.exratio.jme.devkit.registration.spatial;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.AssetLinkNode;
import com.jme3.scene.Node;
import fr.exratio.jme.devkit.config.DevKitConfig;
import fr.exratio.jme.devkit.forms.AddLinkedAsset;
import fr.exratio.jme.devkit.forms.RemoveLinkedAsset;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.SceneTreeService;
import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.tree.spatial.NodeTreeNode;
import fr.exratio.jme.devkit.tree.spatial.menu.NodeContextMenu;
import java.awt.HeadlessException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.tree.TreeNode;

public class AssetLinkNodeRegistrar extends NodeRegistrar {

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

    @Override
    public JPopupMenu getContextMenu() {
      return new AssetLinkNodeContextMenu(this);
    }
  }

  private static class AssetLinkNodeContextMenu extends NodeContextMenu {

    public AssetLinkNodeContextMenu(AssetLinkNodeTreeNode assetLinkNodeTreeNode)
        throws HeadlessException {
      super(assetLinkNodeTreeNode);

      add(new JSeparator());

      JMenuItem addLinkedChildItem = getAddMenu().add(new JMenuItem("Linked Asset"));
      addLinkedChildItem.addActionListener(e -> {

        AddLinkedAsset addLinkedAsset = new AddLinkedAsset(assetLinkNodeTreeNode, DevKitConfig.getInstance().getAssetRootDir());

        JFrame mainWindow = (JFrame) SwingUtilities
            .getWindowAncestor(ServiceManager.getService(EditorJmeApplication.class).getAWTPanel());

        JDialog dialog = new JDialog(mainWindow, "Add Linked Asset", true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(addLinkedAsset.$$$getRootComponent$$$());
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

      });

      JMenuItem removeLinkedChildItem = add(new JMenuItem("Remove Linked Asset"));
      removeLinkedChildItem.addActionListener(e -> {

        RemoveLinkedAsset addLinkedAsset = new RemoveLinkedAsset(assetLinkNodeTreeNode);

        JFrame mainWindow = (JFrame) SwingUtilities
            .getWindowAncestor(ServiceManager.getService(EditorJmeApplication.class).getAWTPanel());

        JDialog dialog = new JDialog(mainWindow, "Remove Linked Asset", true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(addLinkedAsset.$$$getRootComponent$$$());
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

      });

      JMenuItem reloadAssets = add(new JMenuItem("Reload Linked Assets"));
      reloadAssets.addActionListener(e -> {

        // According to the JavaDoc this will reload all assets.
        EditorJmeApplication engineService = ServiceManager.getService(EditorJmeApplication.class);
        engineService.enqueue(() -> {
          assetLinkNodeTreeNode.getUserObject()
              .attachLinkedChildren(engineService.getAssetManager());

          SwingUtilities.invokeLater(() -> {
            ServiceManager.getService(SceneTreeService.class).reloadTreeNode(assetLinkNodeTreeNode);
          });

        });

      });

    }

  }

}
