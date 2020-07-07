package com.jayfella.importer.tree.spatial.menu;

import com.jayfella.importer.forms.AddLinkedAsset;
import com.jayfella.importer.forms.RemoveLinkedAsset;
import com.jayfella.importer.service.JmeEngineService;
import com.jayfella.importer.service.SceneTreeService;
import com.jayfella.importer.service.ServiceManager;
import com.jayfella.importer.tree.spatial.AssetLinkNodeTreeNode;

import javax.swing.*;
import java.awt.*;

public class AssetLinkNodeContextMenu extends SpatialContextMenu {

    public AssetLinkNodeContextMenu(AssetLinkNodeTreeNode spatialTreeNode) throws HeadlessException {
        super(spatialTreeNode);

        add(new JSeparator());

        JMenuItem addLinkedChildItem = getAddMenu().add(new JMenuItem("Linked Asset"));
        addLinkedChildItem.addActionListener(e -> {

            AddLinkedAsset addLinkedAsset = new AddLinkedAsset(spatialTreeNode);

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

            RemoveLinkedAsset addLinkedAsset = new RemoveLinkedAsset(spatialTreeNode);

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
                spatialTreeNode.getUserObject().attachLinkedChildren(engineService.getAssetManager());
                ServiceManager.getService(SceneTreeService.class).reloadTreeNode(spatialTreeNode);
            });

        });

    }


}
