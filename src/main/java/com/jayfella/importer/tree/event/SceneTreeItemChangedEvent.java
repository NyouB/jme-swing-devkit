package com.jayfella.importer.tree.event;

import com.jayfella.importer.event.Event;
import com.jayfella.importer.tree.JmeTreeNode;

/**
 * Fired whenever the scene tree selected item is changed.
 * This event is fired from the SWING thread.
 */
public class SceneTreeItemChangedEvent extends Event {

    private final JmeTreeNode treeNode;

    public SceneTreeItemChangedEvent(JmeTreeNode treeNode) {
        this.treeNode = treeNode;
    }

    public JmeTreeNode getTreeNode() {
        return treeNode;
    }
}
