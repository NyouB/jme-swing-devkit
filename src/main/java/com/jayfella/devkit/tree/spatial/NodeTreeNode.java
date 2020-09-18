package com.jayfella.devkit.tree.spatial;

import com.jayfella.devkit.tree.spatial.menu.NodeContextMenu;
import com.jme3.scene.Node;

import javax.swing.*;

public class NodeTreeNode extends SpatialTreeNode {

    public NodeTreeNode(Node node) {
        super(node);

    }

    @Override
    public Node getUserObject() {
        return (Node) super.getUserObject();
    }

    @Override
    public JPopupMenu getContextMenu() {
        return new NodeContextMenu(this);
    }

}
