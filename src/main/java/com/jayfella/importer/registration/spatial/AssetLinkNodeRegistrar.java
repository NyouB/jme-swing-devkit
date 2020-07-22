package com.jayfella.importer.registration.spatial;

import com.jayfella.importer.forms.AddLinkedAsset;
import com.jayfella.importer.forms.RemoveLinkedAsset;
import com.jayfella.importer.service.JmeEngineService;
import com.jayfella.importer.service.SceneTreeService;
import com.jayfella.importer.service.ServiceManager;
import com.jayfella.importer.tree.spatial.NodeTreeNode;
import com.jayfella.importer.tree.spatial.menu.NodeContextMenu;
import com.jme3.app.SimpleApplication;
import com.jme3.scene.AssetLinkNode;
import com.jme3.scene.Node;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import java.awt.*;

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

        public AssetLinkNodeContextMenu(AssetLinkNodeTreeNode assetLinkNodeTreeNode) throws HeadlessException {
            super(assetLinkNodeTreeNode);

            add(new JSeparator());

            JMenuItem addLinkedChildItem = getAddMenu().add(new JMenuItem("Linked Asset"));
            addLinkedChildItem.addActionListener(e -> {

                AddLinkedAsset addLinkedAsset = new AddLinkedAsset(assetLinkNodeTreeNode);

                JFrame mainWindow = (JFrame) SwingUtilities.getWindowAncestor(ServiceManager.getService(JmeEngineService.class).getCanvas());

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

                JFrame mainWindow = (JFrame) SwingUtilities.getWindowAncestor(ServiceManager.getService(JmeEngineService.class).getCanvas());

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
                JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);
                engineService.enqueue(() -> {
                    assetLinkNodeTreeNode.getUserObject().attachLinkedChildren(engineService.getAssetManager());

                    SwingUtilities.invokeLater(() -> {
                        ServiceManager.getService(SceneTreeService.class).reloadTreeNode(assetLinkNodeTreeNode);
                    });

                });

            });

        }

    }

}
