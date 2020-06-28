package com.jayfella.importer.swing;

import com.jayfella.importer.config.DevKitConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class WindowSizeSaver implements ComponentListener {

    private final String windowId;

    public WindowSizeSaver(String windowId) {
        this.windowId = windowId;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        JFrame frame = (JFrame) e.getComponent();
        Dimension size = frame.getSize();

        DevKitConfig.getInstance().getSdkConfig().setWindowDimensions(windowId, size);
        DevKitConfig.getInstance().save();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
