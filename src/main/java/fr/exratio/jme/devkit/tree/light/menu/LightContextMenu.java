package fr.exratio.jme.devkit.tree.light.menu;

import fr.exratio.jme.devkit.service.SceneTreeService;
import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.tree.light.LightTreeNode;
import fr.exratio.jme.devkit.tree.spatial.SpatialTreeNode;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.springframework.stereotype.Component;

public abstract class LightContextMenu extends JPopupMenu {

  private final LightTreeNode lightTreeNode;

  public LightContextMenu(LightTreeNode lightTreeNode) {
    super("Light");

    this.lightTreeNode = lightTreeNode;

    JMenuItem deleteItem = add(new JMenuItem("Delete"));
    deleteItem.addActionListener(e -> {

      SpatialTreeNode parent = (SpatialTreeNode) lightTreeNode.getParent();
      ServiceManager.getService(SceneTreeService.class).removeTreeNode(lightTreeNode, parent);

    });

  }


}
