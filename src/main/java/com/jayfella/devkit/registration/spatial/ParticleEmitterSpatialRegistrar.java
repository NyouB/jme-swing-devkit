package com.jayfella.devkit.registration.spatial;

import com.jayfella.devkit.tree.spatial.GeometryTreeNode;
import com.jayfella.devkit.tree.spatial.menu.GeometryContextMenu;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

import javax.swing.*;
import javax.swing.tree.TreeNode;

public class ParticleEmitterSpatialRegistrar extends GeometryRegistrar {

    public ParticleEmitterSpatialRegistrar() {
        setRegisteredClass(ParticleEmitter.class);
    }

    @Override
    public Geometry createInstance(SimpleApplication application) {

        AssetManager assetManager = application.getAssetManager();

        ParticleEmitter particleEmitter = new ParticleEmitter("New ParticleEmitter", ParticleMesh.Type.Triangle, 30);

        Material mat_red = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture("Particles/flame.png"));

        particleEmitter.setMaterial(mat_red);
        particleEmitter.setImagesX(2);
        particleEmitter.setImagesY(2); // 2x2 texture animation
        particleEmitter.setEndColor(  new ColorRGBA(1f, 0f, 0f, 1f));   // red
        particleEmitter.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        particleEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
        particleEmitter.setStartSize(1.5f);
        particleEmitter.setEndSize(0.1f);
        particleEmitter.setGravity(0, 0, 0);
        particleEmitter.setLowLife(1f);
        particleEmitter.setHighLife(3f);
        particleEmitter.getParticleInfluencer().setVelocityVariation(0.3f);

        return particleEmitter;
    }

    @Override
    public TreeNode createSceneTreeNode(Geometry geometry, SimpleApplication application) {

        return new GeometryTreeNode(geometry) {
            @Override
            public JPopupMenu getContextMenu() {
                return new GeometryContextMenu(this);
            }
        };

    }

}
