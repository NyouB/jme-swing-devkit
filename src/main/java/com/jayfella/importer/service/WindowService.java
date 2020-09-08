package com.jayfella.importer.service;

import com.jayfella.importer.config.DevKitConfig;

import java.awt.*;

/**
 * A simple service to set window positions and sizes from saved settings.
 */
public class WindowService implements Service {

    private final long threadId;

    public WindowService() {
        threadId = Thread.currentThread().getId();
    }

    /**
     * Sets the position of a window from saved data.
     * @param window the window to position.
     * @param name   the name of the window.
     */
    public void positionWindowFromSavedPosition(Window window, String name) {
            Point location = DevKitConfig.getInstance().getSdkConfig().getWindowLocation(name);
            if (location != null) {
                window.setLocation(location);
            }
    }

    /**
     * Resizes the window from saved data.
     * @param window the window to resize.
     * @param name   the name of the window.
     */
    public void sizeWindowFromSavedSize(Window window, String name) {
        Dimension dimension = DevKitConfig.getInstance().getSdkConfig().getWindowDimensions(name);
        if (dimension != null) {
            window.setSize(dimension);
        }
    }

    @Override
    public long getThreadId() {
        return threadId;
    }

    @Override
    public void stop() {

    }

}
