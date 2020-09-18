package com.jayfella.devkit.registration.spatial;

import com.jayfella.devkit.service.JmeEngineService;
import com.jayfella.devkit.service.SceneTreeService;
import com.jayfella.devkit.service.ServiceManager;
import com.jayfella.devkit.tree.spatial.NodeTreeNode;
import com.jayfella.devkit.tree.spatial.menu.NodeContextMenu;
import com.jme3.app.SimpleApplication;
import com.jme3.scene.BatchNode;
import com.jme3.scene.Node;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import java.awt.*;

public class BatchNodeRegistrar extends NodeRegistrar {

    public BatchNodeRegistrar() {
        super(BatchNode.class);
    }

    @Override
    public Node createInstance(SimpleApplication application) {
        return new BatchNode();
    }

    @Override
    public TreeNode createSceneTreeNode(Node instance, SimpleApplication application) {
        return null;
    }

    public static class BatchNodeTreeNode extends NodeTreeNode {

        public BatchNodeTreeNode(BatchNode node) {
            super(node);
        }

        @Override
        public BatchNode getUserObject() {
            return (BatchNode) super.getUserObject();
        }

        @Override
        public JPopupMenu getContextMenu() {
            return new BatchNodeContextMenu(this);
        }

    }

    public static class BatchNodeContextMenu extends NodeContextMenu {

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

}
