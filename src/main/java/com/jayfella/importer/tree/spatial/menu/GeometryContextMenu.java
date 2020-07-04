package com.jayfella.importer.tree.spatial.menu;

import com.jayfella.importer.service.JmeEngineService;
import com.jayfella.importer.service.ServiceManager;
import com.jayfella.importer.tree.spatial.GeometryTreeNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.instancing.InstancedNode;

import javax.swing.*;
import java.awt.*;

public class GeometryContextMenu extends SpatialContextMenu {

    public GeometryContextMenu(GeometryTreeNode geometryTreeNode) throws HeadlessException {
        super(geometryTreeNode);

        // Determine if the geometry of this mesh is a child of an InstancedNode
        // If it is, give the user the option to create an instance based on this mesh.

        JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);

        JSeparator separator = (JSeparator) add(new JSeparator());

        JMenuItem newInstanceItem = add(new JMenuItem("Create New Instance"));
        newInstanceItem.addActionListener(e -> {

        });
        newInstanceItem.setEnabled(false);

        // the parent is attached to the JME scene, so we must touch it on the JMEt thread.
        engineService.enqueue(() -> {

            // this can only be true.
            // GeometryTreeNode geometryTreeNode = (GeometryTreeNode) meshTreeNode.getParent();
            Geometry geometry = geometryTreeNode.getUserObject();

            Spatial parent = geometry.getParent();
            boolean isInstancedNode = false;

            while (!isInstancedNode && parent != null) {
                isInstancedNode = parent instanceof InstancedNode;
                parent = parent.getParent();
            }

            boolean finalIsInstancedNode = isInstancedNode;

            SwingUtilities.invokeLater(() -> {
                if (finalIsInstancedNode) {
                    newInstanceItem.setEnabled(true);
                }
                else {
                    //remove(loadingItem);
                    newInstanceItem.setVisible(false);
                    separator.setVisible(false);
                }

                // this doesn't seem to work. We'll leave it here for now.
                // The layout manager still retains its height of the invisible elements.
                revalidate();
                repaint();

            });

        });

    }

}
