package com.jayfella.importer.swing;

import com.jayfella.importer.forms.DebugLights;
import com.jayfella.importer.service.ServiceManager;
import com.jayfella.importer.service.WindowService;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Windows {

    public static JFrame createDebugLightsWindow(JCheckBoxMenuItem checkBoxMenuItem) {

        DebugLights debugLights = new DebugLights();

        JFrame frame = new JFrame(DebugLights.DEBUG_LIGHTS_WINDOW_TITLE);
        frame.setContentPane(debugLights.$$$getRootComponent$$$());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowServiceListener());
        frame.addComponentListener(new WindowLocationSaver(DebugLights.DEBUG_LIGHTS_WINDOW_TITLE));
        frame.setResizable(false);
        frame.pack();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                checkBoxMenuItem.setSelected(false);
            }
        });

        ServiceManager.getService(WindowService.class).positionWindowFromSavedPosition(frame, DebugLights.DEBUG_LIGHTS_WINDOW_TITLE);

        return frame;

    }

}
