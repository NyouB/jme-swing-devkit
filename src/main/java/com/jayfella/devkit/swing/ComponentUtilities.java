package com.jayfella.devkit.swing;

import java.awt.*;

public class ComponentUtilities {

    /**
     * Recursively enables or disables components. Used primarily when something is loading to disable the rootPanel.
     * This method **must** be executed on the AWT thread.
     *
     * @param container the container holding the components
     * @param enable    whether or not to set them all as enabled or disabled.
     */
    public static void enableComponents(Container container, boolean enable) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            component.setEnabled(enable);
            if (component instanceof Container) {
                enableComponents((Container)component, enable);
            }
        }
    }

}
