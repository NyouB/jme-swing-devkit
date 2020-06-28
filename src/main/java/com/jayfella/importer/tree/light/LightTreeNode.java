package com.jayfella.importer.tree.light;

import com.jayfella.importer.tree.menu.TreeContextMenu;
import com.jme3.light.Light;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class LightTreeNode extends DefaultMutableTreeNode implements TreeContextMenu {

    public LightTreeNode(Light light) {
        super(light);
    }

    @Override
    public Light getUserObject() {
        return (Light) super.getUserObject();
    }

}
