package com.jayfella.importer.swing;

import com.jayfella.importer.config.DevKitConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class WindowLocationSaver implements ComponentListener {

    private final String windowId;

    public WindowLocationSaver(String windowId) {
        this.windowId = windowId;
    }

    @Override
    public void componentResized(ComponentEvent e) {

    }

    @Override
    public void componentMoved(ComponentEvent e) {
        JFrame frame = (JFrame) e.getComponent();
        Point location = frame.getLocation();

        DevKitConfig.getInstance().getSdkConfig().setWindowLocation(windowId, location);
        DevKitConfig.getInstance().save();
    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
