package com.jayfella.importer.swing;

import com.jayfella.importer.forms.DebugLights;
import com.jayfella.importer.service.ServiceManager;
import com.jayfella.importer.service.WindowService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Windows {

    public static JDialog createDebugLightsWindow(Window parent, JCheckBoxMenuItem checkBoxMenuItem) {

        DebugLights debugLights = new DebugLights();

        JDialog dialog = new JDialog(parent, DebugLights.DEBUG_LIGHTS_WINDOW_TITLE);
        dialog.setContentPane(debugLights.$$$getRootComponent$$$());
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.addComponentListener(new WindowLocationSaver(DebugLights.DEBUG_LIGHTS_WINDOW_TITLE));
        dialog.setResizable(false);
        dialog.pack();

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                checkBoxMenuItem.setSelected(false);
            }
        });

        ServiceManager.getService(WindowService.class).positionWindowFromSavedPosition(dialog, DebugLights.DEBUG_LIGHTS_WINDOW_TITLE);

        return dialog;

    }

}
