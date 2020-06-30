package com.jayfella.importer.service;

import com.jayfella.importer.properties.PropertySection;
import com.jayfella.importer.properties.component.SdkComponent;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PropertyInspectorService implements Service {

    public static final String WINDOW_ID = "Property Inspector";

    private final JPanel rootPanel;
    // private JmeObject selectedObject;


    private JTabbedPane tabbedPane;

    private JLabel nothingSelectedLabel = new JLabel("Nothing To Inspect.");

    public PropertyInspectorService(JPanel rootPanel) {
        this.rootPanel = rootPanel;
        this.tabbedPane = new JTabbedPane();

        clearInspector();
    }

    public void clearInspector() {

        rootPanel.remove(tabbedPane);
        rootPanel.add(nothingSelectedLabel, BorderLayout.CENTER);

    }

    /**
     * Displays all attributes for a given object.
     * This method **must** be called from the AWT thread.
     * @param
     */
    public void inspect(java.util.List<PropertySection> propertySections) {

        if (propertySections != null) {
            rootPanel.remove(nothingSelectedLabel);
            rootPanel.add(tabbedPane);
            tabbedPane.removeAll();

            for (PropertySection section : propertySections) {

                JPanel panel = new JPanel(new VerticalLayout());
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

    @Override
    public void stop() {

    }

}
