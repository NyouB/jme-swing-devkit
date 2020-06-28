package com.jayfella.importer.tree.light;

import com.jayfella.importer.tree.menu.DirectionalLightContextMenu;
import com.jme3.light.DirectionalLight;

import javax.swing.*;

public class DirectionalLightTreeNode extends LightTreeNode {

    public DirectionalLightTreeNode(DirectionalLight light) {
        super(light);
    }

    @Override
    public DirectionalLight getUserObject() {
        return (DirectionalLight) super.getUserObject();
    }

    @Override
    public JPopupMenu getContextMenu() {
        return new DirectionalLightContextMenu(this);
    }
}
