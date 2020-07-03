package com.jayfella.importer.tree.spatial.menu;

import com.jayfella.importer.service.JmeEngineService;
import com.jayfella.importer.service.SceneTreeService;
import com.jayfella.importer.service.ServiceManager;
import com.jayfella.importer.tree.spatial.BatchNodeTreeNode;

import javax.swing.*;
import java.awt.*;

public class BatchNodeContextMenu extends NodeContextMenu {

    public BatchNodeContextMenu(BatchNodeTreeNode nodeTreeNode) throws HeadlessException {
        super(nodeTreeNode);

        add(new JSeparator());

        JMenuItem batchItem = add(new JMenuItem("Batch"));
        batchItem.addActionListener(e -> ServiceManager.getService(JmeEngineService.class).enqueue(() -> {
            nodeTreeNode.getUserObject().batch();
            ServiceManager.getService(SceneTreeService.class).reloadTreeNode(nodeTreeNode);
        }));

    }

}
