package com.jayfella.importer.tree.spatial;

import com.jme3.scene.Mesh;

import javax.swing.tree.DefaultMutableTreeNode;

public class MeshTreeNode extends DefaultMutableTreeNode {

    public MeshTreeNode(Mesh mesh) {
        super(mesh);
    }

    @Override
    public Mesh getUserObject() {
        return (Mesh) super.getUserObject();
    }
    
}
