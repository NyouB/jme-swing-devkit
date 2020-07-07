package com.jayfella.importer.jme;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.debug.Arrow;
import com.jme3.util.BufferUtils;
import jme3tools.optimize.GeometryBatchFactory;

import java.util.Arrays;

/**
 * Displays the rotation of the camera.
 */
public class CameraRotationWidgetState extends BaseAppState {

    private float size = 30f;
    private Spatial widget;
    private final Vector3f location = new Vector3f(0, 0, 0);

    @Override
    protected void initialize(Application app) {

        widget = create();

    }

    private Spatial create() {

        Node node = new Node("Camera Rotation");

        Arrow arrow_mesh_x = new Arrow(new Vector3f(size, 0, 0));
        Arrow arrow_mesh_y = new Arrow(new Vector3f(0, size, 0));
        Arrow arrow_mesh_z = new Arrow(new Vector3f(0, 0, size));

        ColorRGBA[] colors_x = new ColorRGBA[arrow_mesh_x.getVertexCount()];
        Arrays.fill(colors_x, ColorRGBA.Red);

        ColorRGBA[] colors_y = new ColorRGBA[arrow_mesh_x.getVertexCount()];
        Arrays.fill(colors_y, ColorRGBA.Green);

        ColorRGBA[] colors_z = new ColorRGBA[arrow_mesh_x.getVertexCount()];
        Arrays.fill(colors_z, ColorRGBA.Blue);

        arrow_mesh_x.setBuffer(VertexBuffer.Type.Color, 4, BufferUtils.createFloatBuffer(colors_x));
        arrow_mesh_y.setBuffer(VertexBuffer.Type.Color, 4, BufferUtils.createFloatBuffer(colors_y));
        arrow_mesh_z.setBuffer(VertexBuffer.Type.Color, 4, BufferUtils.createFloatBuffer(colors_z));

        Geometry arrow_geom_x = new Geometry("Arrow X", arrow_mesh_x);
        Geometry arrow_geom_y = new Geometry("Arrow Y", arrow_mesh_y);
        Geometry arrow_geom_z = new Geometry("Arrow Z", arrow_mesh_z);

        Material material = new Material(getApplication().getAssetManager(), Materials.UNSHADED);
        material.setBoolean("VertexColor", true);
        material.getAdditionalRenderState().setLineWidth(2);

        arrow_geom_x.setMaterial(material);
        arrow_geom_y.setMaterial(material);
        arrow_geom_z.setMaterial(material);

        node.attachChild(arrow_geom_x);
        node.attachChild(arrow_geom_y);
        node.attachChild(arrow_geom_z);

        return GeometryBatchFactory.optimize(node, false);
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {

        Node guiNode = ((SimpleApplication)getApplication()).getGuiNode();
        guiNode.attachChild(widget);

    }

    @Override
    protected void onDisable() {
        widget.removeFromParent();
    }

    @Override
    public void update(float tpf) {
        widget.setLocalRotation(getApplication().getCamera().getRotation().inverse());

        location.set(size + 10,
                getApplication().getCamera().getHeight() - size - 10,
                1);

        widget.setLocalTranslation(location);
    }

}
