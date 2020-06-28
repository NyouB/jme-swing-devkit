package com.jayfella.importer.service;

import com.jayfella.importer.config.DevKitConfig;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple service to keep track of all open windows so we can dispose of them when the application closes.
 */
public class WindowService implements Service {

    private final List<Window> openWindows = new ArrayList<>();

    public void add(Window window) {
        openWindows.add(window);
    }

    public void remove(Window window) {
        openWindows.remove(window);
    }

    public Window getWindow(String title) {

        return openWindows.stream()
                .filter(w -> w instanceof JFrame)
                .filter(w -> ((JFrame)w).getTitle().equals(title))
                .findFirst()
                .orElse(null);

    }

    public void positionWindowFromSavedPosition(JFrame frame, String name) {
            Point location = DevKitConfig.getInstance().getSdkConfig().getWindowLocation(name);
            if (location != null) {
                frame.setLocation(location);
            }
    }

    public void sizeWindowFromSavedSize(JFrame frame, String name) {
        Dimension dimension = DevKitConfig.getInstance().getSdkConfig().getWindowDimensions(name);
        if (dimension != null) {
            frame.setSize(dimension);
        }
    }

    @Override
    public void stop() {
        openWindows.forEach(Window::dispose);
        openWindows.clear();
    }

}
