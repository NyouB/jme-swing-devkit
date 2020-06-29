package com.jayfella.importer.tree.menu;

import com.jayfella.importer.forms.GenerateLightProbeDialog;
import com.jayfella.importer.forms.SaveSpatial;
import com.jayfella.importer.jme.EditorCameraState;
import com.jayfella.importer.service.JmeEngineService;
import com.jayfella.importer.service.SceneTreeService;
import com.jayfella.importer.service.ServiceManager;
import com.jayfella.importer.swing.WindowServiceListener;
import com.jayfella.importer.tree.TreeConstants;
import com.jayfella.importer.tree.spatial.SpatialTreeNode;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import javax.swing.*;
import java.awt.*;

public class SpatialContextMenu extends JPopupMenu {

    private final Spatial spatial;
    SpatialTreeNode spatialTreeNode;
    private final JMenu addMenu;

    public SpatialContextMenu(final SpatialTreeNode spatialTreeNode) throws HeadlessException {
        super();

        this.spatialTreeNode = spatialTreeNode;
        this.spatial = spatialTreeNode.getUserObject();

        JMenuItem lookAtItem = add(new JMenuItem("Look at Spatial"));
        lookAtItem.addActionListener(e -> {
            JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);
            // engineService.enqueue(() -> engineService.getCamera().lookAt(spatial.getWorldTranslation(), Vector3f.UNIT_Y));
            engineService.getStateManager().getState(EditorCameraState.class).lookAt(spatial.getWorldTranslation(), Vector3f.UNIT_Y);
        });

        addMenu = createAddMenu();
        add(addMenu);

        add(new JSeparator());

        JMenuItem saveItem = add(new JMenuItem("Save..."));
        saveItem.addActionListener(e -> {

            SaveSpatial saveSpatial = new SaveSpatial(spatial);

            JFrame frame = new JFrame("Save Spatial");
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.addWindowListener(new WindowServiceListener());
            frame.setContentPane(saveSpatial.$$$getRootComponent$$$());
            frame.setLocationRelativeTo(null);
            frame.pack();

            frame.setVisible(true);

        });
        saveItem.setMnemonic('S');

        add(new JSeparator());

        JMenuItem deleteItem = add(new JMenuItem("Delete"));
        deleteItem.addActionListener(e -> {

            // we must only query scene object in the JME thread,
            ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

                // verify we're allowed to delete this object.
                final String undeletable = spatial.getUserData(TreeConstants.UNDELETABLE_FLAG);

                SwingUtilities.invokeLater(() -> {

                    if (undeletable == null) {
                        ServiceManager.getService(SceneTreeService.class).removeTreeNode(spatialTreeNode);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "You are not allowed to delete this spatial.",
                                "Delete Rejected",
                                JOptionPane.ERROR_MESSAGE);
                    }

                });


            });

        });
        deleteItem.setMnemonic('D');

    }

    public JMenu getAddMenu() {
        return addMenu;
    }

    private JMenu createAddMenu() {

        JMenu menu = new JMenu("Add...");
        menu.setMnemonic('A');

        JMenu lightMenu = createLightMenu();
        menu.add(lightMenu);

        return menu;
    }

    private JMenu createLightMenu() {

        JMenu menu = new JMenu("Light...");
        menu.setMnemonic('L');

        JMenuItem ambLight = menu.add(new JMenuItem("Ambient"));
        ambLight.addActionListener(e -> {
            ServiceManager.getService(SceneTreeService.class).addLight(new AmbientLight(), spatialTreeNode);
        });

        JMenuItem dirLight = menu.add(new JMenuItem("Directional"));
        dirLight.addActionListener(e -> {
            DirectionalLight directionalLight = new DirectionalLight(new Vector3f(-1, -1, -1).normalizeLocal());
            ServiceManager.getService(SceneTreeService.class).addLight(directionalLight, spatialTreeNode);
        });

        JMenuItem pointLight = menu.add(new JMenuItem("Point"));
        pointLight.addActionListener(e -> {

            // This is a bit misleading because of multi-threading. I can't query the camera location on the AWT thread
            // but I need to set its position to the camera position, so it will be set when it's attached - which will
            // be on the JME thread.
            PointLight light = new PointLight(new Vector3f(0, 0, 0), 10);
            ServiceManager.getService(SceneTreeService.class).addLight(light, spatialTreeNode);

        });

        JMenuItem probeLight = menu.add(new JMenuItem("Generate LightProbe..."));
        probeLight.addActionListener(e -> {

            GenerateLightProbeDialog generateLightProbeDialog = new GenerateLightProbeDialog(spatialTreeNode);

            JFrame frame = new JFrame("Generate LightProbe");
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setContentPane(generateLightProbeDialog.$$$getRootComponent$$$());
            frame.addWindowListener(new WindowServiceListener());
            frame.pack();
            frame.setLocationRelativeTo(null);

            frame.setVisible(true);

        });

        return menu;
    }

}
