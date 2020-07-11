package com.jayfella.importer.tree.spatial.menu;

import com.jayfella.importer.config.DevKitConfig;
import com.jayfella.importer.forms.AddModels;
import com.jayfella.importer.forms.CreateSkyBoxDialog;
import com.jayfella.importer.service.ClipboardService;
import com.jayfella.importer.service.JmeEngineService;
import com.jayfella.importer.service.SceneTreeService;
import com.jayfella.importer.service.ServiceManager;
import com.jayfella.importer.tree.spatial.NodeTreeNode;
import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.*;
import com.jme3.scene.instancing.InstancedNode;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Dome;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;

import javax.swing.*;
import java.awt.*;


public class NodeContextMenu extends SpatialContextMenu {

    private final NodeTreeNode nodeTreeNode;

    public NodeContextMenu(NodeTreeNode nodeTreeNode) throws HeadlessException {
        super(nodeTreeNode);

        this.nodeTreeNode = nodeTreeNode;

        // Add -> Shape
        JMenu addShapeMenu = (JMenu) getAddMenu().add(new JMenu("Shape..."));
        addShapes(addShapeMenu);
        addShapeMenu.setMnemonic('S');

        // Add -> Model(s)...
        JMenuItem addModelsItem = getAddMenu().add(new JMenuItem("Model(s)..."));
        addModelsItem.addActionListener(e -> {

            AddModels addModels = new AddModels(nodeTreeNode);

            JFrame mainWindow = (JFrame) SwingUtilities.getWindowAncestor(ServiceManager.getService(JmeEngineService.class).getCanvas());

            JDialog dialog = new JDialog(mainWindow, "Add Model(s)", true);
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog.setContentPane(addModels.$$$getRootComponent$$$());
            dialog.pack();
            dialog.setLocationRelativeTo(mainWindow);
            dialog.setVisible(true);

        });
        addModelsItem.setMnemonic('M');

        // Add -> SkyBox...
        JMenuItem genSkyBoxItem = getAddMenu().add(new JMenuItem("SkyBox..."));
        genSkyBoxItem.addActionListener(e -> {

            CreateSkyBoxDialog createSkyBoxDialog = new CreateSkyBoxDialog(nodeTreeNode);

            JFrame mainWindow = (JFrame) SwingUtilities.getWindowAncestor(ServiceManager.getService(JmeEngineService.class).getCanvas());

            JDialog dialog = new JDialog(mainWindow, "Create SkyBox", true);
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog.setContentPane(createSkyBoxDialog.$$$getRootComponent$$$());
            dialog.pack();
            dialog.setLocationRelativeTo(mainWindow);
            dialog.setVisible(true);

        });
        genSkyBoxItem.setMnemonic('K');

        // Add -> Particle
        JMenuItem particeItem = getAddMenu().add(new JMenuItem("ParticleEmitter"));
        particeItem.addActionListener(e -> {

            AssetManager assetManager = ServiceManager.getService(JmeEngineService.class).getAssetManager();

            Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
            mat.setTexture("Texture", assetManager.loadTexture("Particles/flame.png"));

            ParticleEmitter particleEmitter = new ParticleEmitter("New ParticleEmitter", ParticleMesh.Type.Triangle, 100);
            particleEmitter.setMaterial(mat);
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

            ServiceManager.getService(SceneTreeService.class).addSpatial(particleEmitter, nodeTreeNode);

        });

        // Add -> Node
        JMenuItem addNodeItem = getAddMenu().add(new JMenuItem("Node"));
        addNodeItem.addActionListener(e -> ServiceManager.getService(SceneTreeService.class).addSpatial(new Node(), nodeTreeNode));
        addNodeItem.setMnemonic('N');

        // Add -> InstancedNode
        JMenuItem addInstancedNodeItem = getAddMenu().add(new JMenuItem("Instanced Node"));
        addInstancedNodeItem.addActionListener(e -> ServiceManager.getService(SceneTreeService.class).addSpatial(new InstancedNode("No Name"), nodeTreeNode));

        // Add -> AssetLinkNode
        JMenuItem addLinkNodeItem = getAddMenu().add(new JMenuItem("AssetLink Node"));
        addLinkNodeItem.addActionListener(e -> {

            // Just add an AssetLinkNode and provide a context menu to add linked assets.
            ServiceManager.getService(SceneTreeService.class).addSpatial(new AssetLinkNode(), nodeTreeNode);

        });

        // Add -> BatchNode
        JMenuItem batchNodeItem = getAddMenu().add(new JMenuItem("Batch Node"));
        batchNodeItem.addActionListener(e -> ServiceManager.getService(SceneTreeService.class).addSpatial(new BatchNode(), nodeTreeNode));

        JMenuItem pasteItem = add(new JMenuItem("Paste"));
        pasteItem.setEnabled(ServiceManager.getService(ClipboardService.class).hasSpatialClipboardItem());
        pasteItem.addActionListener(e -> {

            Spatial clonedSpatial = ServiceManager.getService(ClipboardService.class).getSpatialClipboardItem().getClonedCopy();
            ServiceManager.getService(SceneTreeService.class).addSpatial(clonedSpatial, nodeTreeNode);

        });

    }

    private void addShapes(JMenu parent) {

        JMenuItem cubeItem = parent.add(new JMenuItem("Cube"));
        cubeItem.addActionListener(e -> {
            ServiceManager.getService(SceneTreeService.class).addSpatial(
                    createShape(new com.jme3.scene.shape.Box(1, 1, 1), "Cube"), nodeTreeNode);
        });

        JMenuItem cylinderItem = parent.add(new JMenuItem("Cylinder"));
        cylinderItem.addActionListener(e -> {
            ServiceManager.getService(SceneTreeService.class).addSpatial(
                    createShape(new Cylinder(32, 32, 1.0f, 1.0f, true), "Cylinder"), nodeTreeNode);
        });

        JMenuItem domeItem = parent.add(new JMenuItem("Dome"));
        domeItem.addActionListener(e -> {
            ServiceManager.getService(SceneTreeService.class).addSpatial(
                    createShape(new Dome(32, 32, 1.0f), "Dome"), nodeTreeNode);
        });

        JMenuItem quadItem = parent.add(new JMenuItem("Quad"));
        quadItem.addActionListener(e -> {
            ServiceManager.getService(SceneTreeService.class).addSpatial(
                    createShape(new Quad(1.0f, 1.0f), "Quad"), nodeTreeNode);
        });

        JMenuItem sphereItem = parent.add(new JMenuItem("Sphere"));
        sphereItem.addActionListener(e -> {
            ServiceManager.getService(SceneTreeService.class).addSpatial(
                    createShape(new Sphere(32, 32, 1.0f), "Sphere"), nodeTreeNode);
        });

    }

    private Geometry createShape(Mesh mesh, String name) {

        JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);

        Geometry geometry = new Geometry(name, mesh);

        Material material = new Material(engineService.getAssetManager(), DevKitConfig.getInstance().getSdkConfig().getDefaultMaterial());
        geometry.setMaterial(material);

        return geometry;
    }

}
