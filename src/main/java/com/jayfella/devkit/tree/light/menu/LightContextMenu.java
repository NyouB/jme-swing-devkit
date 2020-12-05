package com.jayfella.devkit.tree.light.menu;

import com.jayfella.devkit.service.SceneTreeService;
import com.jayfella.devkit.service.ServiceManager;
import com.jayfella.devkit.tree.light.LightTreeNode;
import com.jayfella.devkit.tree.spatial.SpatialTreeNode;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

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
