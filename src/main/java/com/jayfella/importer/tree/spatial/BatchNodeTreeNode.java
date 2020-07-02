package com.jayfella.importer.tree.spatial;

import com.jayfella.importer.tree.menu.BatchNodeContextMenu;
import com.jme3.scene.BatchNode;

import javax.swing.*;

public class BatchNodeTreeNode extends NodeTreeNode {

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
