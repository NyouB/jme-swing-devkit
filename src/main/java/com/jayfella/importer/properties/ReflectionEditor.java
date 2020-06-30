package com.jayfella.importer.properties;

import com.jayfella.importer.properties.component.SdkComponent;
import com.jayfella.importer.properties.reflection.ComponentBuilder;
import com.jayfella.importer.properties.reflection.UniqueProperties;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;

public class ReflectionEditor implements JmeObject {

    private final JPanel contentPanel;

    // this reflection editor needs to return multiple JPanels that can be added to the JTabbedPanel.
    // We are looking for:
    // - Spatial : transform, shadowmode, name, etc.
    // - Geometry : ignoreTransform, lodLevel
    // - Material : material properties
    // - Other : Other properties (ParticleEmitters, etc).

    // And for controls they'll be custom.
    public ReflectionEditor(Object object, String... ignoredProperties) {

        this.contentPanel = new JPanel(new VerticalLayout());

        UniqueProperties uniqueProperties = new UniqueProperties(object, ignoredProperties);
        ComponentBuilder componentBuilder = new ComponentBuilder(uniqueProperties);

        // @todo: register external components for get/set return types that are not registered.
        // componentBuilder.registerComponent(MyReturnType.class, MyComponent.class);

        componentBuilder.build();

        for (SdkComponent sdkComponent : componentBuilder.getSdkComponents()) {
            contentPanel.add(sdkComponent.getJComponent());
        }

        // if this is a Spatial it will have a getControl and getLightList
        if (object instanceof Spatial) {

            Spatial spatial = (Spatial) object;

            int controlsCount = spatial.getNumControls();

            if (controlsCount > 0) {

                for (int i = 0; i < controlsCount; i++) {

                    Control control = spatial.getControl(i);

                    UniqueProperties controlUniqueProperties = new UniqueProperties(control);
                    ComponentBuilder controlComponentBuilder = new ComponentBuilder(controlUniqueProperties);
                    controlComponentBuilder.build();

                    for (SdkComponent sdkComponent : controlComponentBuilder.getSdkComponents()) {
                        contentPanel.add(sdkComponent.getJComponent());
                    }
                }

            }

        }
    }

    @Override
    public JComponent getJComponent() {
        return contentPanel;
    }

    @Override
    public void cleanup() {

    }

}
