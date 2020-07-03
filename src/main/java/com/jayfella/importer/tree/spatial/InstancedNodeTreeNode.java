package com.jayfella.importer.tree.spatial;

import com.jayfella.importer.tree.spatial.menu.InstancedNodeContextMenu;
import com.jme3.scene.instancing.InstancedNode;

import javax.swing.*;

public class InstancedNodeTreeNode extends SpatialTreeNode {

    public InstancedNodeTreeNode(InstancedNode instancedNode) {
        super(instancedNode);
    }

    @Override
    public InstancedNode getUserObject() {
        return (InstancedNode) super.getUserObject();
    }

    @Override
    public JPopupMenu getContextMenu() {
        return new InstancedNodeContextMenu(this);
    }
}
