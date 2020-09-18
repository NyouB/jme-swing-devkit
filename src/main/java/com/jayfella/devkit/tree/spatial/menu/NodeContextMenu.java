package com.jayfella.devkit.tree.spatial.menu;

import com.jayfella.devkit.config.DevKitConfig;
import com.jayfella.devkit.forms.AddModels;
import com.jayfella.devkit.forms.CreateSkyBoxDialog;
import com.jayfella.devkit.registration.spatial.GeometryRegistrar;
import com.jayfella.devkit.registration.spatial.NodeRegistrar;
import com.jayfella.devkit.service.*;
import com.jayfella.devkit.tree.spatial.NodeTreeNode;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Dome;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;


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

        // Add -> Registered Spatials
        RegistrationService registrationService = ServiceManager.getService(RegistrationService.class);
        Set<NodeRegistrar> nodeRegistrars = registrationService.getNodeRegistration().getRegistrations();

        if (!nodeRegistrars.isEmpty()) {

            getAddMenu().add(new JSeparator());

            for (NodeRegistrar registrar : nodeRegistrars) {

                JMenuItem menuItem = getAddMenu().add(new JMenuItem(registrar.getRegisteredClass().getSimpleName()));

                menuItem.addActionListener(e -> {

                    Node node = registrar.createInstance(ServiceManager.getService(JmeEngineService.class));
                    ServiceManager.getService(SceneTreeService.class).addSpatial(node, nodeTreeNode);

                });

            }
        }

        Set<GeometryRegistrar> geometryRegistrars = registrationService.getGeometryRegistration().getRegistrations();
        if (!geometryRegistrars.isEmpty()) {

            getAddMenu().add(new JSeparator());

            for (GeometryRegistrar registrar : geometryRegistrars) {

                JMenuItem menuItem = getAddMenu().add(new JMenuItem(registrar.getRegisteredClass().getSimpleName()));

                menuItem.addActionListener(e -> {

                    Geometry geometry = registrar.createInstance(ServiceManager.getService(JmeEngineService.class));
                    ServiceManager.getService(SceneTreeService.class).addSpatial(geometry, nodeTreeNode);

                });

            }

        }

        add(new JSeparator());

        JMenuItem pasteItem = add(new JMenuItem("Paste"));
        pasteItem.setEnabled(ServiceManager.getService(ClipboardService.class).hasSpatialClipboardItem());
        pasteItem.addActionListener(e -> {

            Spatial clonedSpatial = ServiceManager.getService(ClipboardService.class).getSpatialClipboardItem().getClonedCopy();
            ServiceManager.getService(SceneTreeService.class).addSpatial(clonedSpatial, nodeTreeNode);

        });

        // Allow users to also add their options....
        List<JMenuItem> customItems = ServiceManager.getService(MenuService.class)
                .getCustomMenuItems(NodeTreeNode.class);

        if (customItems != null && !customItems.isEmpty()) {

            // add a separator for clarity.
            add(new JSeparator());

            for (JMenuItem customItem : customItems) {
                add(customItem);
            }
        }
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
