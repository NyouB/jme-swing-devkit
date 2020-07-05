package com.jayfella.importer.tree.spatial.menu;

import com.jayfella.importer.config.DevKitConfig;
import com.jayfella.importer.forms.AddModels;
import com.jayfella.importer.forms.CreateSkyBoxDialog;
import com.jayfella.importer.service.ClipboardService;
import com.jayfella.importer.service.JmeEngineService;
import com.jayfella.importer.service.SceneTreeService;
import com.jayfella.importer.service.ServiceManager;
import com.jayfella.importer.swing.WindowServiceListener;
import com.jayfella.importer.tree.spatial.NodeTreeNode;
import com.jme3.material.Material;
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

            JFrame frame = new JFrame("Add Model(s)");
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setContentPane(addModels.$$$getRootComponent$$$());
            frame.addWindowListener(new WindowServiceListener());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        });
        addModelsItem.setMnemonic('M');

        // Add -> SkyBox...
        JMenuItem genSkyBoxItem = getAddMenu().add(new JMenuItem("SkyBox..."));
        genSkyBoxItem.addActionListener(e -> {

            CreateSkyBoxDialog createSkyBoxDialog = new CreateSkyBoxDialog(nodeTreeNode);

            JFrame frame = new JFrame("Create SkyBox");
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setContentPane(createSkyBoxDialog.$$$getRootComponent$$$());
            frame.addWindowListener(new WindowServiceListener());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        });
        genSkyBoxItem.setMnemonic('K');

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
