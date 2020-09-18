package com.jayfella.devkit.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jayfella.devkit.core.ColorConverter;
import com.jayfella.devkit.service.JmeEngineService;
import com.jayfella.devkit.service.ServiceManager;
import com.jme3.light.*;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import javax.swing.*;
import javax.swing.colorchooser.DefaultColorSelectionModel;
import java.awt.*;

public class DebugLights {

    public static final String DEBUG_LIGHTS_WINDOW_TITLE = "Debug Lights";

    private JCheckBox ambientCheckBox;
    private JCheckBox directionalCheckBox;
    private JCheckBox probeCheckBox;
    private JComboBox<DemoProbe> probesComboBox;
    private JPanel rootPanel;
    private JTabbedPane tabbedPane1;
    private JColorChooser ambientColorChooser;
    private JColorChooser directionalColorChooser;

    private AmbientLight ambientLight;
    private DirectionalLight directionalLight;
    private LightProbe lightProbe;


    public DebugLights() {

        // we don't want preview panels.
        ambientColorChooser.setPreviewPanel(new JPanel());
        directionalColorChooser.setPreviewPanel(new JPanel());

        // we don't need opacity with lights.
//        for (AbstractColorChooserPanel ccPanel : ambientColorChooser.getChooserPanels()) {
//            ccPanel.setColorTransparencySelectionEnabled(
//                    false);
//        }
//
//        for (AbstractColorChooserPanel ccPanel : directionalColorChooser.getChooserPanels()) {
//            ccPanel.setColorTransparencySelectionEnabled(
//                    false);
//        }

        DefaultComboBoxModel<DemoProbe> listModel = new DefaultComboBoxModel<>();
        for (DemoProbe demoProbe : DemoProbe.values()) {
            listModel.addElement(demoProbe);
        }
        probesComboBox.setModel(listModel);
        probesComboBox.addActionListener(e -> {

            final boolean isSelected = probeCheckBox.isSelected();

            JComboBox<DemoProbe> comboBox = (JComboBox<DemoProbe>) e.getSource();
            final DemoProbe demoProbe = (DemoProbe) comboBox.getSelectedItem();

            JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);

            engineService.enqueue(() -> {

                if (lightProbe != null) {
                    engineService.getRootNode().removeLight(lightProbe);
                }

                if (isSelected && demoProbe != null) {
                    lightProbe = demoProbe.extractProbe();
                    engineService.getRootNode().addLight(lightProbe);
                }

            });

        });

        ambientColorChooser.getSelectionModel().addChangeListener(e -> {
            DefaultColorSelectionModel colorChooser = (DefaultColorSelectionModel) e.getSource();
            final ColorRGBA colorRGBA = ColorConverter.toColorRGBA(colorChooser.getSelectedColor());

            ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

                if (ambientLight != null) {
                    ambientLight.setColor(colorRGBA);
                }

            });

        });

        directionalColorChooser.getSelectionModel().addChangeListener(e -> {
            DefaultColorSelectionModel colorChooser = (DefaultColorSelectionModel) e.getSource();
            final ColorRGBA colorRGBA = ColorConverter.toColorRGBA(colorChooser.getSelectedColor());

            ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

                if (directionalLight != null) {
                    directionalLight.setColor(colorRGBA);
                }

            });

        });

        ambientCheckBox.addActionListener(e -> {

            JCheckBox checkBox = (JCheckBox) e.getSource();

            final boolean isSelected = checkBox.isSelected();
            final ColorRGBA colorRGBA = ColorConverter.toColorRGBA(ambientColorChooser.getColor());

            final JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);

            engineService.enqueue(() -> {

                if (isSelected) {

                    if (ambientLight == null) {
                        ambientLight = new AmbientLight(colorRGBA);
                        engineService.getRootNode().addLight(ambientLight);
                    } else {
                        ambientLight.setColor(colorRGBA);
                    }
                } else {

                    if (ambientLight != null) {
                        engineService.getRootNode().removeLight(ambientLight);
                        ambientLight = null;
                    }
                }

            });

        });

        directionalCheckBox.addActionListener(e -> {

            JCheckBox checkBox = (JCheckBox) e.getSource();

            final boolean isSelected = checkBox.isSelected();
            final ColorRGBA colorRGBA = ColorConverter.toColorRGBA(directionalColorChooser.getColor());

            final JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);

            engineService.enqueue(() -> {

                if (isSelected) {

                    if (directionalLight == null) {
                        directionalLight = new DirectionalLight(new Vector3f(-1, -1, -1).normalizeLocal(), colorRGBA);
                        engineService.getRootNode().addLight(directionalLight);
                    } else {
                        directionalLight.setColor(colorRGBA);
                    }
                } else {

                    if (directionalLight != null) {
                        engineService.getRootNode().removeLight(directionalLight);
                        directionalLight = null;
                    }
                }

            });

        });

        probeCheckBox.addActionListener(e -> {

            JCheckBox checkBox = (JCheckBox) e.getSource();

            final boolean isSelected = checkBox.isSelected();
            final DemoProbe demoProbe = (DemoProbe) probesComboBox.getSelectedItem();

            final JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);


            engineService.enqueue(() -> {

                if (isSelected) {

                    if (lightProbe != null) {
                        engineService.getRootNode().removeLight(lightProbe);
                    }

                    if (demoProbe != null) {
                        lightProbe = demoProbe.extractProbe();
                        engineService.getRootNode().addLight(lightProbe);
                    }

                } else {
                    if (lightProbe != null) {
                        engineService.getRootNode().removeLight(lightProbe);
                        lightProbe = null;
                    }
                }

            });

        });

        querySceneForDebugLights();
    }

    private void querySceneForDebugLights() {

        JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);

        engineService.enqueue(() -> {

            // read the lights from the JME thread.
            LightList lights = engineService.getRootNode().getLocalLightList();

            for (Light light : lights) {

                if (light instanceof AmbientLight) {

                    // set the ambientLight reference from the JME thread.
                    ambientLight = (AmbientLight) light;
                    final ColorRGBA colorRGBA = ambientLight.getColor();

                    // set the JavaFX value from the JavaFX thread.
                    SwingUtilities.invokeLater(() -> {
                        ambientCheckBox.setSelected(true);
                        ambientColorChooser.setColor(ColorConverter.toColor(colorRGBA));
                    });
                } else if (light instanceof DirectionalLight) {

                    // set the ambientLight reference from the JME thread.
                    directionalLight = (DirectionalLight) light;
                    final ColorRGBA colorRGBA = directionalLight.getColor();

                    // set the JavaFX value from the JavaFX thread.
                    SwingUtilities.invokeLater(() -> {
                        directionalCheckBox.setSelected(true);
                        directionalColorChooser.setColor(ColorConverter.toColor(colorRGBA));
                    });
                } else if (light instanceof LightProbe) {

                    // set the ambientLight reference from the JME thread.
                    lightProbe = (LightProbe) light;
                    final String name = lightProbe.getName();

                    if (name != null) {

                        // set the JavaFX value from the JavaFX thread.
                        SwingUtilities.invokeLater(() -> {
                            probeCheckBox.setSelected(true);
                            //DemoProbe demoProbe = DemoProbe.fromResourcePath(name);
                            //probeChoiceBox.getSelectionModel().select(demoProbe);
                        });

                    }

                }

            }

        });

    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1 = new JTabbedPane();
        rootPanel.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Ambient", panel1);
        ambientCheckBox = new JCheckBox();
        ambientCheckBox.setText("Enabled");
        panel1.add(ambientCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ambientColorChooser = new JColorChooser();
        panel1.add(ambientColorChooser, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Directional", panel2);
        directionalCheckBox = new JCheckBox();
        directionalCheckBox.setText("Enabled");
        panel2.add(directionalCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        directionalColorChooser = new JColorChooser();
        panel2.add(directionalColorChooser, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("LightProbe", panel3);
        probeCheckBox = new JCheckBox();
        probeCheckBox.setText("Enabled");
        panel3.add(probeCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Probe");
        panel3.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        probesComboBox = new JComboBox();
        panel3.add(probesComboBox, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        rootPanel.add(spacer3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        rootPanel.add(spacer4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

    private enum DemoProbe {

        Bathroom("Probes/bathroom.j3o"),
        City_Night_Lights("Probes/City_Night_Lights.j3o"),
        Corsica_Beach("Probes/corsica_beach.j3o"),
        Dresden_Station_Night("Probes/dresden_station_night.j3o"),
        Flower_Road("Probes/flower_road.j3o"),
        Glass_Passage("Probes/glass_passage.j3o"),
        Parking_Lot("Probes/Parking_Lot.j3o"),
        River_Road("Probes/River_Road.j3o"),
        Road_In_Tenerife_Mountain("Probes/road_in_tenerife_mountain.j3o"),
        Sky_Cloudy("Probes/Sky_Cloudy.j3o"),
        StoneWall("Probes/Stonewall.j3o"),
        Studio("Probes/studio.j3o");

        private final String resourcePath;

        DemoProbe(String resourcePath) {
            this.resourcePath = resourcePath;
        }

        public String getResourcePath() {
            return resourcePath;
        }

        public LightProbe extractProbe() {

            Spatial probeHolder = ServiceManager.getService(JmeEngineService.class)
                    .getAssetManager()
                    .loadModel(getResourcePath());

            LightProbe lightProbe = (LightProbe) probeHolder.getLocalLightList().get(0);
            probeHolder.removeLight(lightProbe);

            lightProbe.getArea().setRadius(500);
            lightProbe.setName(resourcePath);

            return lightProbe;
        }

        public static DemoProbe fromResourcePath(String resourcePath) {

            for (DemoProbe demoProbe : values()) {
                if (demoProbe.getResourcePath().equals(resourcePath)) {
                    return demoProbe;
                }
            }

            return null;
        }

    }

}
