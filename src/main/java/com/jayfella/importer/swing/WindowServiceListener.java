package com.jayfella.importer.swing;

import com.jayfella.importer.service.ServiceManager;
import com.jayfella.importer.service.WindowService;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A service that allows us to keep track of all open windows so we can close them on exit.
 * All open windows must add this listener.
 */
public class WindowServiceListener extends WindowAdapter {

    @Override
    public void windowOpened(WindowEvent e) {
        ServiceManager.getService(WindowService.class).add(e.getWindow());
    }

    @Override
    public void windowClosed(WindowEvent e) {
        ServiceManager.getService(WindowService.class).remove(e.getWindow());
    }

}
