package fr.exratio.devkit.tree.control;

import fr.exratio.devkit.service.SceneTreeService;
import fr.exratio.devkit.service.ServiceManager;
import fr.exratio.devkit.tree.spatial.SpatialTreeNode;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ControlContextMenu extends JPopupMenu {

  public ControlContextMenu(final ControlTreeNode controlTreeNode) {
    super();

    JMenuItem deleteItem = add(new JMenuItem("Delete"));
    deleteItem.addActionListener(e -> {
      SpatialTreeNode parent = (SpatialTreeNode) controlTreeNode.getParent();
      ServiceManager.getService(SceneTreeService.class).removeTreeNode(controlTreeNode, parent);
    });
    deleteItem.setMnemonic('D');

  }

}
