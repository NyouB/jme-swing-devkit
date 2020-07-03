package com.jayfella.importer.tree.spatial.menu;

import com.jayfella.importer.forms.AddLinkedAsset;
import com.jayfella.importer.forms.RemoveLinkedAsset;
import com.jayfella.importer.service.JmeEngineService;
import com.jayfella.importer.service.SceneTreeService;
import com.jayfella.importer.service.ServiceManager;
import com.jayfella.importer.swing.WindowServiceListener;
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

            JFrame frame = new JFrame("Add Linked Asset");
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setContentPane(addLinkedAsset.$$$getRootComponent$$$());
            frame.addWindowListener(new WindowServiceListener());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        });

        JMenuItem removeLinkedChildItem = add(new JMenuItem("Remove Linked Asset"));
        removeLinkedChildItem.addActionListener(e -> {

            RemoveLinkedAsset addLinkedAsset = new RemoveLinkedAsset(spatialTreeNode);

            JFrame frame = new JFrame("Remove Linked Asset");
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setContentPane(addLinkedAsset.$$$getRootComponent$$$());
            frame.addWindowListener(new WindowServiceListener());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

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
