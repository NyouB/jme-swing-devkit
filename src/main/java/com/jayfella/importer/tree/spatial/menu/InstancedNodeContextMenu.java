package com.jayfella.importer.tree.spatial.menu;

import com.jayfella.importer.service.JmeEngineService;
import com.jayfella.importer.service.ServiceManager;
import com.jayfella.importer.tree.spatial.InstancedNodeTreeNode;

import javax.swing.*;
import java.awt.*;

public class InstancedNodeContextMenu extends NodeContextMenu {

    public InstancedNodeContextMenu(InstancedNodeTreeNode instancedNodeTreeNode) throws HeadlessException {
        super(instancedNodeTreeNode);

        JMenuItem instanceItem = add(new JMenuItem("Instance Items"));
        instanceItem.addActionListener(e -> {
            ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

                try {
                    instancedNodeTreeNode.getUserObject().instance();
                }
                catch (IllegalStateException ex) {

                    JOptionPane.showMessageDialog(null,
                            ex.getMessage(),
                            "Instancing Error",
                            JOptionPane.ERROR_MESSAGE);

                }

            });
        });

    }

}
