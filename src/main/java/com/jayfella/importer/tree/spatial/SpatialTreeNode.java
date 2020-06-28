package com.jayfella.importer.tree.spatial;

import com.jayfella.importer.tree.menu.TreeContextMenu;
import com.jme3.scene.Spatial;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class SpatialTreeNode extends DefaultMutableTreeNode implements TreeContextMenu {

    public SpatialTreeNode(Spatial spatial) {
        super(spatial);
    }

    @Override
    public Spatial getUserObject() {
        return (Spatial) super.getUserObject();
    }

}
