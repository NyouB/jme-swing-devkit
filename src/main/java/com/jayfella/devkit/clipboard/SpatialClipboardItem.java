package com.jayfella.devkit.clipboard;

import com.jme3.material.Material;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.util.clone.Cloner;
import com.jme3.util.clone.IdentityCloneFunction;

public class SpatialClipboardItem {

    private final Spatial spatial;
    private final boolean cloneMaterial, cloneMesh;

    /**
     * Creates a clipboard item with the default clone actions.
     * @param spatial The spatial to put into the clipboard.
     */
    public SpatialClipboardItem(Spatial spatial) {
        this(spatial, true, false);
    }

    public SpatialClipboardItem(Spatial spatial, boolean cloneMaterial, boolean cloneMesh) {
        this.spatial = spatial;
        this.cloneMaterial = cloneMaterial;
        this.cloneMesh = cloneMesh;
    }

    public Spatial getSpatial() {
        return spatial;
    }

    public boolean isCloneMesh() {
        return cloneMesh;
    }

    public boolean isCloneMaterial() {
        return cloneMaterial;
    }

    public Spatial getClonedCopy() {
        return deepClone(spatial, cloneMaterial, cloneMesh);
    }

    /**
     * Clones a spatial, optionally cloning materials or meshes.
     * @param spatial       the spatial to clone
     * @param cloneMaterial whether or not to clone the material
     * @param cloneMesh     whether or not to clone the mesh
     * @return a cloned copy of the spatial.
     */
    private Spatial deepClone(Spatial spatial, boolean cloneMaterial, boolean cloneMesh) {

        // Setup the cloner for the type of cloning we want to do.
        Cloner cloner = new Cloner();

        // First, we definitely do not want to clone our own parent
        cloner.setClonedValue(spatial.getParent(), null);

        // If we aren't cloning materials then we will make sure those
        // aren't cloned also
        if( !cloneMaterial ) {
            cloner.setCloneFunction(Material.class, new IdentityCloneFunction<>());
        }

        // By default the meshes are not cloned.  The geometry
        // may choose to selectively force them to be cloned but
        // normally they will be shared
        if ( !cloneMesh ) {
            cloner.setCloneFunction(Mesh.class, new IdentityCloneFunction<>());
        }

        Spatial clone = cloner.clone(spatial);

        // Because we've nulled the parent out we need to make sure
        // the transforms and stuff get refreshed.
        clone.forceRefresh(true, true, true);
        // clone.setTransformRefresh();
        // clone.setLightListRefresh();
        // clone.setMatParamOverrideRefresh();

        return clone;

    }

}
