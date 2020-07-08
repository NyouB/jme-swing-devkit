package com.jayfella.importer.service;

import com.jayfella.importer.properties.PropertySection;
import com.jayfella.importer.properties.builder.ReflectedComponentSetBuilder;
import com.jayfella.importer.properties.builder.SpatialComponentSetBuilder;
import com.jayfella.importer.properties.component.SdkComponent;
import com.jayfella.importer.properties.component.control.AnimComposerComponent;
import com.jayfella.importer.properties.component.control.AnimControlComponent;
import com.jme3.anim.AnimComposer;
import com.jme3.animation.AnimControl;
import com.jme3.scene.Spatial;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PropertyInspectorService implements Service {

    public static final String WINDOW_ID = "Property Inspector";

    private final JPanel rootPanel;
    private final JTabbedPane tabbedPane = new JTabbedPane();

    private final JLabel nothingSelectedLabel = new JLabel("Nothing To Inspect.");

    private Collection<PropertySection> displayedSections;

    public PropertyInspectorService(JPanel rootPanel) {
        this.rootPanel = rootPanel;
        clearInspector();
    }

    public void clearInspector() {
        rootPanel.remove(tabbedPane);
        rootPanel.add(nothingSelectedLabel, BorderLayout.CENTER);
        rootPanel.repaint();
    }

    /**
     * Displays all attributes for a given object.
     * This method **must** be called from the AWT thread.
     * @param propertySections The list of property sections to display.
     */
    private void displaySections(Collection<PropertySection> propertySections) {

        displayedSections = propertySections;

        if (propertySections != null) {
            rootPanel.remove(nothingSelectedLabel);
            rootPanel.add(tabbedPane);
            tabbedPane.removeAll();

            for (PropertySection section : propertySections) {

                JPanel panel = new JPanel(new VerticalLayout(5));
                panel.setBorder(new EmptyBorder(10, 0, 0, 0));

                for (SdkComponent component : section.getComponents()) {
                    panel.add(component.getJComponent());
                }

                tabbedPane.addTab(section.getTitle(), section.getIcon(), panel);
            }

        }
        else {
            clearInspector();
        }

        rootPanel.revalidate();
        rootPanel.repaint();
    }

    /**
     * Inspects a given object.
     * This method **must** be called from the AWT thread.
     * @param object the object to inspect.
     */
    public void inspect(Object object) {

        cleanup();

        if (object instanceof Spatial) {

            Spatial spatial = (Spatial) object;
            SpatialComponentSetBuilder componentSetBuilder = new SpatialComponentSetBuilder(spatial);

            List<PropertySection> propertySections = componentSetBuilder.build();
            displaySections(propertySections);

        }
        else if (object instanceof AnimComposer) {
            AnimComposer animComposer = (AnimComposer) object;
            AnimComposerComponent animComposerComponent = new AnimComposerComponent(animComposer);

            PropertySection propertySection = new PropertySection("AnimComposer", animComposerComponent);

            List<PropertySection> propertySections = new ArrayList<>();
            propertySections.add(propertySection);
            displaySections(propertySections);
        }
        else if (object instanceof AnimControl) {
            AnimControl animControl = (AnimControl) object;
            AnimControlComponent animControlComponent = new AnimControlComponent(animControl);

            PropertySection propertySection = new PropertySection("AnimControl", animControlComponent);

            List<PropertySection> propertySections = new ArrayList<>();
            propertySections.add(propertySection);
            displaySections(propertySections);

        }
        else {

            // we don't know what it is, so all we can do is display reflected properties.

            ReflectedComponentSetBuilder componentSetBuilder = new ReflectedComponentSetBuilder(
                    object.getClass().getSimpleName(), object);

            List<PropertySection> propertySections = componentSetBuilder.build();
            displaySections(propertySections);

        }

    }

    private void cleanup() {

        if (displayedSections != null) {

            for (PropertySection section : displayedSections) {

                SdkComponent[] components = section.getComponents();

                if (components != null) {
                    for (SdkComponent component : components) {
                        component.cleanup();
                    }
                }
            }
        }
    }

    @Override
    public void stop() {

    }

}
