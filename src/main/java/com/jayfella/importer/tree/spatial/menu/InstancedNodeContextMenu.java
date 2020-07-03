package com.jayfella.importer.tree.spatial.menu;

import com.jayfella.importer.service.JmeEngineService;
import com.jayfella.importer.service.ServiceManager;
import com.jayfella.importer.tree.spatial.InstancedNodeTreeNode;

import javax.swing.*;
import java.awt.*;

public class InstancedNodeContextMenu extends SpatialContextMenu {

    public InstancedNodeContextMenu(InstancedNodeTreeNode instancedNodeTreeNode) throws HeadlessException {
        super(instancedNodeTreeNode);

        JMenuItem instanceItem = add(new JMenuItem("Instance Items"));
        instanceItem.addActionListener(e -> {
            ServiceManager.getService(JmeEngineService.class).enqueue(() -> instancedNodeTreeNode.getUserObject().instance());
        });

    }

}
