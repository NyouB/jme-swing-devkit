package com.jayfella.importer.service;

import com.jayfella.importer.config.DevKitConfig;

import java.awt.*;

/**
 * A simple service to set window positions and sizes from saved settings.
 */
public class WindowService implements Service {

    public void positionWindowFromSavedPosition(Window window, String name) {
            Point location = DevKitConfig.getInstance().getSdkConfig().getWindowLocation(name);
            if (location != null) {
                window.setLocation(location);
            }
    }

    public void sizeWindowFromSavedSize(Window window, String name) {
        Dimension dimension = DevKitConfig.getInstance().getSdkConfig().getWindowDimensions(name);
        if (dimension != null) {
            window.setSize(dimension);
        }
    }

    @Override
    public void stop() {

    }

}
