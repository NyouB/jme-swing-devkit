package com.jayfella.importer.tree.light;

import com.jayfella.importer.tree.light.menu.AmbientLightContextMenu;
import com.jme3.light.AmbientLight;

import javax.swing.*;

public class AmbientLightTreeNode extends LightTreeNode {

    public AmbientLightTreeNode(AmbientLight light) {
        super(light);
    }

    @Override
    public AmbientLight getUserObject() {
        return (AmbientLight) super.getUserObject();
    }

    @Override
    public JPopupMenu getContextMenu() {
        return new AmbientLightContextMenu(this);
    }
}
