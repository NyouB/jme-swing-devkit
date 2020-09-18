package com.jayfella.devkit.jme;

import com.jayfella.devkit.service.JmeEngineService;
import com.jayfella.devkit.service.ServiceManager;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.light.Light;
import com.jme3.light.LightProbe;
import com.jme3.light.OrientedBoxProbeArea;
import com.jme3.light.SphereProbeArea;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.debug.WireSphere;
import com.jme3.scene.shape.Sphere;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * An appstate that highlights scene objects.
 * All calls to this class **must** be from the JME thread.
 */
public class SceneObjectHighlighterState extends BaseAppState {

    private static final Logger log = Logger.getLogger(SceneObjectHighlighterState.class.getName());

    private static final String HIGHLIGHTER_BOUNDINGVOLUME = "BoundingVolume Highlight";
    private static final String HIGHLIGHTER_MESH = "Mesh Highlight";

    private Material highlightMaterial;

    // this node stays at 0,0,0. All highlighted items are moved and rotated individually as children.
    private final Node highlighterNode = new Node("Highlighter Node");

    // we have the ability to highlight multiple objects.
    private final Map<Spatial, Spatial> highlightedSpatials = new HashMap<>(); // Map<SceneSpatial, HighlighterSpatial>
    private final Map<Light, Spatial> highlightedLights = new HashMap<>(); // Map<SceneLight, HighlighterSpatial>

    public SceneObjectHighlighterState() {

    }

    @Override protected void initialize(Application app) {

        //highlighterNode.setQueueBucket(RenderQueue.Bucket.Transparent);
        highlightMaterial = ServiceManager.getService(JmeEngineService.class).getAssetManager().loadMaterial("Materials/HighlightMaterial.j3m");
        ((SimpleApplication)app).getRootNode().attachChild(highlighterNode);

    }

    @Override protected void cleanup(Application app) { }
    @Override protected void onEnable() { }
    @Override protected void onDisable() { }


    public void highlight(Spatial spatial) {

        Spatial highlighter = makeBoundingGeometry(spatial);

        if (highlighter != null) {
            highlightedSpatials.put(spatial, highlighter);
            highlighterNode.attachChild(highlighter);
        }

    }

    public void highlight(Light light) {

        Spatial highlighter = makeLightGeometry(light);

        if (highlighter != null) {
            highlightedLights.put(light, highlighter);
            highlighterNode.attachChild(highlighter);
        }
    }

    public void highlightMesh(Geometry geometry) {

        // if we clone the geometry we also get all of the controls, which we don't think we want.
        // We use our own material, and sometimes the control changes material values that don't exist.
        // particle emitters, for example.

        // Geometry highlighter = geometry.clone(false);
        Geometry highlighter = new Geometry(HIGHLIGHTER_MESH, geometry.getMesh());
        // highlighter.setName(HIGHLIGHTER_MESH);
        highlighter.setLocalTranslation(geometry.getWorldTranslation());
        highlighter.setLocalRotation(geometry.getWorldRotation());
        highlighter.setLocalScale(geometry.getWorldScale());
        highlighter.setMaterial(highlightMaterial);
        highlighter.setShadowMode(RenderQueue.ShadowMode.Off);

        // objects in the sky bucket, etc can cause visualization issues.
        highlighter.setQueueBucket(RenderQueue.Bucket.Inherit);

        highlightedSpatials.put(geometry, highlighter);
        highlighterNode.attachChild(highlighter);
    }

    public void removeHighlight(Spatial spatial) {

        Spatial highlighter = highlightedSpatials.get(spatial);

        if (highlighter != null) {
            highlighter.removeFromParent();
            highlightedSpatials.remove(spatial);
        }

    }

    private Geometry makeBoundingGeometry(Spatial spatial) {

        Geometry highlighter = null;

        if (spatial.getWorldBound() instanceof BoundingBox) {
            highlighter = makeBoundingBox(spatial);
        }
        else if (spatial.getWorldBound() instanceof BoundingSphere) {
            highlighter = makeBoundingSphere(spatial);
        }
        else {
            log.warning("Unhandled Bounding Shape: " + spatial.getWorldBound().getClass().getName());
        }

        return highlighter;

    }

    private Geometry makeBoundingBox(Spatial spatial) {

        Geometry geometry = WireBox.makeGeometry((BoundingBox) spatial.getWorldBound());
        geometry.setName(HIGHLIGHTER_BOUNDINGVOLUME);

        geometry.setMaterial(highlightMaterial);
        geometry.setShadowMode(RenderQueue.ShadowMode.Off);

        // keep track of rotation and location because bounding volume geometries are positioned in world space and axis aligned.
        // we don't set a location or rotation, but we need to know if they've changed so we can re-create the mesh.
        geometry.setUserData("worldTranslation", geometry.getWorldTranslation());
        geometry.setUserData("worldRotation", geometry.getWorldRotation());

        return geometry;

    }

    private Geometry makeBoundingSphere(Spatial spatial) {

        BoundingSphere boundingSphere = (BoundingSphere) spatial.getWorldBound();
        Geometry geometry = null;

        // things like skyboxes can have an infinite radius. We can't see them anyway, so lets not draw them.
        if (Float.isFinite(boundingSphere.getRadius())) {

            WireSphere wireSphere = new WireSphere(boundingSphere.getRadius());
            geometry = new Geometry(HIGHLIGHTER_BOUNDINGVOLUME, wireSphere);

            geometry.setMaterial(highlightMaterial);
            geometry.setShadowMode(RenderQueue.ShadowMode.Off);

            // keep track of rotation and location because bounding volume geometries are positioned in world space and axis aligned.
            // we don't set a location or rotation, but we need to know if they've changed so we can re-create the mesh.
            geometry.setUserData("worldTranslation", geometry.getWorldTranslation());
            geometry.setUserData("worldRotation", geometry.getWorldRotation());

        }

        return geometry;

    }

    private Spatial makeLightGeometry(Light light) {

        // Directional lights have no location, only a direction. I'm not certain how I'm going to visualize that.
        // Ambient lights are just a color, so we'll probably just skip over that.

        Spatial highlighter = null;

        if (light instanceof LightProbe) {

            LightProbe lightProbe = (LightProbe) light;

            Node probeHighlighterNode = new Node("LightProbe Highlighter");
            probeHighlighterNode.setLocalTranslation(lightProbe.getPosition());

            float radius = lightProbe.getArea().getRadius();

            /// draw the center of the probe to indicate its position.
            Sphere sphere = new Sphere(16, 16, 0.5f);
            Geometry probeCenter = new Geometry("Light Probe Center", sphere);
            probeCenter.setMaterial(new Material(ServiceManager.getService(JmeEngineService.class).getAssetManager(), "Common/MatDefs/Misc/reflect.j3md"));
            probeHighlighterNode.attachChild(probeCenter);

            // we may as well just set this now.
            highlighter = probeHighlighterNode;

            switch (lightProbe.getAreaType()) {

                case OrientedBox: {

                    // oriented boxes can be rotated.

                    WireBox wireBox = new WireBox(radius, radius, radius);
                    Geometry geometry = new Geometry("LightProbe Highlighter", wireBox);
                    geometry.setMaterial(highlightMaterial);
                    probeHighlighterNode.attachChild(geometry);

                    OrientedBoxProbeArea area = (OrientedBoxProbeArea) lightProbe.getArea();

                    // set the rotation according to the lightprobe area.
                    geometry.setLocalRotation(area.getRotation());

                    // I think the center is in world coordinates. We want it local.
                    probeCenter.setLocalTranslation(area.getCenter().subtract(lightProbe.getPosition()));

                    break;
                }
                case Spherical: {

                    WireSphere wireSphere = new WireSphere(radius);
                    Geometry geometry = new Geometry("LightProbe Highlighter", wireSphere);
                    geometry.setMaterial(highlightMaterial);
                    probeHighlighterNode.attachChild(geometry);

                    SphereProbeArea area = (SphereProbeArea) lightProbe.getArea();

                    // I think the center is in world coordinates. We want it local.
                    probeCenter.setLocalTranslation(area.getCenter().subtract(lightProbe.getPosition()));

                    break;
                }

            }

        }

        return highlighter;

    }

    public void removeAllHighlights() {

        highlightedSpatials.clear();
        highlighterNode.detachAllChildren();

    }

    @Override
    public void update(float tpf) {

        for (Map.Entry<Spatial, Spatial> entry : highlightedSpatials.entrySet()) {

            Spatial spatial = entry.getKey();
            Spatial highlighter = entry.getValue();

            switch (highlighter.getName()) {

                case HIGHLIGHTER_BOUNDINGVOLUME : {

                    // boundingvolumes don't have a localTranslation. They are positioned in world coordinates.
                    // if neither of these values are equal, the mesh needs to be re-created.
                    Vector3f worldLocation = highlighter.getUserData("worldTranslation");
                    Quaternion worldRotation = highlighter.getUserData("worldRotation");

                    boolean equal =
                            worldLocation.equals(spatial.getWorldTranslation()) &&
                            worldRotation.equals(spatial.getWorldRotation());

                    if (!equal) {

                        highlighter.removeFromParent();
                        highlighter = makeBoundingGeometry(spatial);
                        highlighterNode.attachChild(highlighter);
                        highlightedSpatials.replace(spatial, highlighter);

                    }

                    break;
                }

                case HIGHLIGHTER_MESH : {

                    if (!highlighter.getLocalTranslation().equals(spatial.getWorldTranslation())) {
                        highlighter.setLocalTranslation(spatial.getWorldTranslation());
                    }

                    if (!highlighter.getLocalRotation().equals(spatial.getWorldRotation())) {
                        highlighter.setLocalRotation(spatial.getWorldRotation());
                    }

                    break;
                }

            }

        }

    }

}
