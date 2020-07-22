package com.jayfella.importer.service;

import com.jayfella.importer.properties.ControlSdkComponent;
import com.jayfella.importer.properties.PropertySection;
import com.jayfella.importer.properties.builder.AbstractComponentSetBuilder;
import com.jayfella.importer.properties.builder.ReflectedComponentSetBuilder;
import com.jayfella.importer.properties.builder.SpatialComponentSetBuilder;
import com.jayfella.importer.properties.component.SdkComponent;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PropertyInspectorService implements Service {

    public static final String WINDOW_ID = "Property Inspector";

    private final JPanel rootPanel;
    private final JTabbedPane tabbedPane = new JTabbedPane();

    private final JLabel nothingSelectedLabel = new JLabel("Nothing To Inspect.");

    private List<PropertySection> displayedSections;

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
    private void displaySections(List<PropertySection> propertySections) {

        displayedSections = propertySections;

        if (propertySections != null) {
            rootPanel.remove(nothingSelectedLabel);
            rootPanel.add(tabbedPane);
            tabbedPane.removeAll();

            for (PropertySection section : propertySections) {
                addTab(section);
            }

        }
        else {
            clearInspector();
        }

        rootPanel.revalidate();
        rootPanel.repaint();
    }

    private void addTab(PropertySection section) {
        addTab(section, tabbedPane.getTabCount());
    }

    private void addTab(PropertySection section, int index) {

        JPanel panel = new JPanel(new VerticalLayout(5));
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));

        for (SdkComponent component : section.getComponents()) {
            panel.add(component.getJComponent());
        }

        tabbedPane.insertTab(section.getTitle(), section.getIcon(), panel, null, index);

    }

    /**
     * Inspects a given object.
     * This method **must** be called from the AWT thread.
     * @param object the object to inspect.
     */
    public void inspect(final Object object) {

        cleanup();

        // check controls
        // check componentBuilders

        boolean found = false;

        if (object instanceof Control) {

            Control control = (Control) object;

            Class<? extends ControlSdkComponent<?>> componentClass = ServiceManager.getService(RegistrationService.class)
                    .getControlSdkComponentFor(control.getClass());

            if (componentClass != null) {

                try {

                    Constructor<? extends ControlSdkComponent<?>> constructor = componentClass.getConstructor(control.getClass());
                    ControlSdkComponent<?> controlComponent = constructor.newInstance(control);

                    PropertySection propertySection = new PropertySection(control.getClass().getSimpleName(), controlComponent);

                    List<PropertySection> propertySections = new ArrayList<>();
                    propertySections.add(propertySection);
                    displaySections(propertySections);

                    found = true;

                } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }

            }

        } else {

            Class<? extends AbstractComponentSetBuilder<?>> componentSetBuilderClass = ServiceManager.getService(RegistrationService.class)
                    .getComponentSetBuilderFor(object.getClass());

            if (componentSetBuilderClass != null) {

                try {

                    Constructor<? extends AbstractComponentSetBuilder<?>> constructor = componentSetBuilderClass.getConstructor(object.getClass());
                    AbstractComponentSetBuilder<?> componentSetBuilder = constructor.newInstance(object);

                    List<PropertySection> sections = componentSetBuilder.build();
                    displaySections(sections);

                    found = true;

                } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }

            }

        }

        if (!found) {

            // we display the spatial component builder last to give everything else a chance to find something.
            // Our last effort is to display the "generic" spatial componentBuilder.
            // This is actually most likely the case for 90% of spatial(s), but for things like ParticleEmitters
            // they need the opportunity to find their own custom builder.
            if (object instanceof Spatial) {

                Spatial spatial = (Spatial) object;
                SpatialComponentSetBuilder componentSetBuilder = new SpatialComponentSetBuilder(spatial);

                List<PropertySection> propertySections = componentSetBuilder.build();
                displaySections(propertySections);

            } else {

                // we don't know what it is, so all we can do is display reflected properties.

                ReflectedComponentSetBuilder componentSetBuilder = new ReflectedComponentSetBuilder(
                        object.getClass().getSimpleName(), object);

                List<PropertySection> propertySections = componentSetBuilder.build();
                displaySections(propertySections);

            }

        }

    }

    public void updateSections(Collection<PropertySection> updatedSections) {

        for (PropertySection updatedSection : updatedSections) {

            int index = -1;

            for (int i = 0; i < displayedSections.size(); i++) {

                PropertySection currentSection = displayedSections.get(i);
                if (currentSection.getTitle().equals(updatedSection.getTitle())) {
                    index = i;
                    break;
                }
            }

            if (index > -1) {
                displayedSections.set(index, updatedSection);
                tabbedPane.remove(index);
                addTab(updatedSection, index);
            }

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

            displayedSections = null;
        }

    }

    @Override
    public void stop() {

    }

}
