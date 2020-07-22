package com.jayfella.importer.tree.control;

import com.jayfella.importer.service.SceneTreeService;
import com.jayfella.importer.service.ServiceManager;
import com.jayfella.importer.tree.spatial.SpatialTreeNode;

import javax.swing.*;

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
