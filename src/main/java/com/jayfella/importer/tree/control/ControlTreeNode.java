package com.jayfella.importer.tree.control;

import com.jayfella.importer.tree.TreeContextMenu;
import com.jme3.scene.control.Control;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class ControlTreeNode extends DefaultMutableTreeNode implements TreeContextMenu {

    public ControlTreeNode(Control control) {
        super(control);
    }

    @Override
    public Control getUserObject() {
        return (Control) super.getUserObject();
    }

    @Override
    public JPopupMenu getContextMenu() {
        return null;
    }

}
