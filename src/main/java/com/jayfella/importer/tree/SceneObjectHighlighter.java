package com.jayfella.importer.tree;

import com.jayfella.importer.service.JmeEngineService;
import com.jayfella.importer.service.ServiceManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.debug.WireSphere;

import java.util.Timer;
import java.util.TimerTask;

public class SceneObjectHighlighter {

    private Spatial spatial;

    private Node highlighterNode = new Node("Spatial Highlighter");
    private Geometry highlightGeom;

    private final Timer timer = new Timer();
    private final long timerDelay = 1000L / 60L;
    private final TimerTask timerTask;

    // keep a copy of rotation because we don't rotate boundingVolumes but we need to know if the spatial has rotated.
    private final  Quaternion rotation = new Quaternion();

    private final Material highlightMaterial;

    public SceneObjectHighlighter() {

        timerTask = new TimerTask() {
            @Override
            public void run() {
                update();
            }
        };

        timer.scheduleAtFixedRate(timerTask, 1L, timerDelay);

        highlightMaterial = new Material(ServiceManager.getService(JmeEngineService.class).getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        highlightMaterial.getAdditionalRenderState().setLineWidth(2);
        highlightMaterial.getAdditionalRenderState().setWireframe(true);
        highlightMaterial.getAdditionalRenderState().setDepthTest(false);
        highlightMaterial.setColor("Color", ColorRGBA.Blue);

        highlighterNode.setQueueBucket(RenderQueue.Bucket.Transparent);
    }

    public void highlightBoundingShape(Spatial spatial) {

        removeHighlight();

        this.spatial = spatial;

        if (spatial.getWorldBound() != null) {
            ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

                if (spatial.getWorldBound() instanceof BoundingBox) {

                    makeBoundingBox();
                    showHightlight();
                }
                else if (spatial.getWorldBound() instanceof BoundingSphere) {

                    makeBoundingSphere();
                    showHightlight();
                }

            });
        }
    }

    private void makeBoundingBox() {

        Geometry geometry = WireBox.makeGeometry((BoundingBox) spatial.getWorldBound());

        geometry.setMaterial(highlightMaterial);
        geometry.getMaterial().getAdditionalRenderState().setLineWidth(2);
        geometry.getMaterial().getAdditionalRenderState().setWireframe(true);
        geometry.getMaterial().setColor("Color", ColorRGBA.Blue);
        geometry.setShadowMode(RenderQueue.ShadowMode.Off);

        ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

            if (this.highlightGeom != null) {
                this.highlightGeom.removeFromParent();
            }

            this.highlightGeom = geometry;
            highlighterNode.attachChild(this.highlightGeom);
        });

    }

    private void makeBoundingSphere() {

        BoundingSphere boundingSphere = (BoundingSphere) spatial.getWorldBound();
        WireSphere wireSphere = new WireSphere(boundingSphere.getRadius());

        Geometry geometry = new Geometry("Bounding Sphere Geometry", wireSphere);
        geometry.setMaterial(highlightMaterial);
        geometry.setShadowMode(RenderQueue.ShadowMode.Off);

        ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

            if (this.highlightGeom != null) {
                this.highlightGeom.removeFromParent();
            }

            this.highlightGeom = geometry;
            highlighterNode.attachChild(this.highlightGeom);
        });

    }

    public void highlightMesh(Geometry geometry) {

        removeHighlight();

        this.spatial = geometry;

        if (geometry != null) {
            ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

                makeMesh();
                showHightlight();

            });
        }
    }

    private void makeMesh() {

        Geometry geom = (Geometry) spatial;

        Geometry geometry = new Geometry("Mesh Highlight", geom.getMesh());
        geometry.setLocalRotation(geometry.getWorldRotation());
        geometry.setLocalTranslation(geometry.getWorldTranslation());
        geometry.setLocalScale(geometry.getWorldScale());
        geometry.setShadowMode(RenderQueue.ShadowMode.Off);

        geometry.setMaterial(highlightMaterial);

        ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

            if (this.highlightGeom != null) {
                this.highlightGeom.removeFromParent();
            }

            this.highlightGeom = geometry;
            highlighterNode.attachChild(this.highlightGeom);
        });

    }

    public void deleteHighlight() {

        spatial = null;

        if (highlightGeom != null) {
            ServiceManager.getService(JmeEngineService.class).enqueue(() -> {
                highlightGeom.removeFromParent();
                highlightGeom = null;
            });
        }
    }

    public void removeHighlight() {

        spatial = null;

        ServiceManager.getService(JmeEngineService.class).enqueue(() -> highlighterNode.removeFromParent());
    }

    public void showHightlight() {
        JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);
        engineService.enqueue(() -> engineService.getRootNode().attachChild(highlighterNode));
    }

    public void update() {

        ServiceManager.getService(JmeEngineService.class).enqueue(() -> {
            if (highlightGeom != null && spatial != null) {


                // if the positions are not the same, move the highlighter NODE.
                // we move the highlighter node because the BoundingVolume geometries have a localTranslation.
                // this means the world translations will not match.
                //if (!spatial.getWorldTranslation().equals(highlightGeom.getWorldTranslation())) {
                    //highlightGeom.setLocalTranslation(spatial.getWorldTranslation());
                //}

                // Don't update the rotation of bounding volumes.
                // When geometries are selected we show the mesh.
                // When nodes are highlighted we show a BoundingVolume.

                // so if the rotation differs, rotate the highlight mesh.
                // if the scale differs, re-create the mesh.

                if (spatial instanceof Geometry) {

                    if (!spatial.getWorldRotation().equals(highlightGeom.getWorldRotation())) {
                        highlightGeom.setLocalRotation(spatial.getWorldRotation());
                    }

                    // if the scale differs, change the scale.
                    if (!spatial.getWorldScale().equals(highlightGeom.getWorldScale())) {
                        highlightGeom.setLocalScale(spatial.getWorldScale());
                    }

                } else {

                    // if the rotation or scale differ, redraw the bounding volume.

                    boolean scaleMatch = spatial.getWorldScale().equals(highlightGeom.getWorldScale());
                    boolean rotationMatch = rotation.equals(spatial.getWorldRotation());

                    if (!scaleMatch || !rotationMatch) {

                        if (spatial.getWorldBound() instanceof BoundingBox) {
                            makeBoundingBox();
                        } else if (spatial.getWorldBound() instanceof BoundingSphere) {
                            makeBoundingSphere();
                        }
                    }

                }

                rotation.set(spatial.getWorldRotation());


            }
        });
    }

    public void stop() {
        timer.cancel();
        timer.purge();
    }

}
