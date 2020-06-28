package com.jayfella.importer.tree.menu;

import com.jayfella.importer.service.SceneTreeService;
import com.jayfella.importer.service.ServiceManager;
import com.jayfella.importer.tree.light.LightTreeNode;
import com.jayfella.importer.tree.spatial.SpatialTreeNode;

import javax.swing.*;

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
