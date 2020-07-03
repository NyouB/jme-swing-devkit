package com.jayfella.importer.tree.spatial;

import com.jayfella.importer.tree.spatial.menu.AssetLinkNodeContextMenu;
import com.jme3.scene.AssetLinkNode;
import com.jme3.scene.Node;

import javax.swing.*;

public class AssetLinkNodeTreeNode extends NodeTreeNode {
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
