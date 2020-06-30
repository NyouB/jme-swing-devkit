package com.jayfella.importer.service;

import com.jayfella.importer.properties.PropertySection;
import com.jayfella.importer.properties.builder.SpatialComponentSetBuilder;
import com.jayfella.importer.properties.component.SdkComponent;
import com.jme3.scene.Spatial;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class PropertyInspectorService implements Service {

    public static final String WINDOW_ID = "Property Inspector";

    private final JPanel rootPanel;
    private final JTabbedPane tabbedPane = new JTabbedPane();

    private JLabel nothingSelectedLabel = new JLabel("Nothing To Inspect.");

    public PropertyInspectorService(JPanel rootPanel) {
        this.rootPanel = rootPanel;
        clearInspector();
    }

    public void clearInspector() {
        rootPanel.remove(tabbedPane);
        rootPanel.add(nothingSelectedLabel, BorderLayout.CENTER);
    }

    /**
     * Displays all attributes for a given object.
     * This method **must** be called from the AWT thread.
     * @param propertySections The list of property sections to display.
     */
    private void displaySections(java.util.List<PropertySection> propertySections) {

        if (propertySections != null) {
            rootPanel.remove(nothingSelectedLabel);
            rootPanel.add(tabbedPane);
            tabbedPane.removeAll();

            for (PropertySection section : propertySections) {

                JPanel panel = new JPanel(new VerticalLayout(10));
                panel.setBorder(new EmptyBorder(10, 0, 0, 0));

                for (SdkComponent component : section.getComponents()) {
                    panel.add(component.getJComponent());
                }

                tabbedPane.addTab(section.getTitle(), null, panel);
            }

        }
        else {
            clearInspector();
        }


        rootPanel.repaint();
        rootPanel.revalidate();
    }

    /**
     * Inspects a given object.
     * This method **must** be called from the AWT thread.
     * @param object the object to inspect.
     */
    public void inspect(Object object) {

        if (object instanceof Spatial) {

            Spatial spatial = (Spatial) object;
            SpatialComponentSetBuilder<Spatial> componentSetBuilder = new SpatialComponentSetBuilder<>();
            componentSetBuilder.setObject(spatial);

            List<PropertySection> propertySections = componentSetBuilder.build();
            ServiceManager.getService(PropertyInspectorService.class).displaySections(propertySections);

        }
        else {
            clearInspector();
        }


    }

    @Override
    public void stop() {

    }

}
