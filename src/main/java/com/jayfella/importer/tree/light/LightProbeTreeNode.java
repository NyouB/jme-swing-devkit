package com.jayfella.importer.tree.light;

import com.jayfella.importer.tree.light.menu.LightProbeContextMenu;
import com.jme3.light.LightProbe;

import javax.swing.*;

public class LightProbeTreeNode extends LightTreeNode {

    public LightProbeTreeNode(LightProbe light) {
        super(light);
    }

    @Override
    public LightProbe getUserObject() {
        return (LightProbe) super.getUserObject();
    }

    @Override
    public JPopupMenu getContextMenu() {
        return new LightProbeContextMenu(this);
    }

}
