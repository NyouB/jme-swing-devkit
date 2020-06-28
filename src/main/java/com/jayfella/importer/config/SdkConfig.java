package com.jayfella.importer.config;

import com.jayfella.importer.forms.MainPage;
import com.jayfella.importer.service.SceneTreeService;

import java.awt.*;
import java.beans.Transient;
import java.util.HashMap;

public class SdkConfig {

    private HashMap<String, Dimension> windowDimensions = new HashMap<>();
    private HashMap<String, Point> windowLocations = new HashMap<>();

    // private int[] mainWindowSize = new int[] { 800, 600 }; // the size of the main window.
    // private int[] mainWindowLocation = new int[] { -1, -1 }; // the starting location of the window.

    // private int[] treeWindowSize = new int[] { 250, 600 }; // the size of the main window.
    // private int[] treeWindowLocation = new int[] { 0, 0 }; // the starting location of the window.

    private boolean showCamRotationWidget = true;
    private boolean showDebugLightsWindow = false;

    public SdkConfig() {

        // default values
        windowDimensions.putIfAbsent(MainPage.WINDOW_ID, new Dimension(800, 600));
        windowDimensions.putIfAbsent(SceneTreeService.WINDOW_ID, new Dimension(250, 600));

        // put the scene tree in the top left if there is no value.
        // since the main window has none set, it will be put in the center of the screen.
        windowLocations.putIfAbsent(SceneTreeService.WINDOW_ID, new Point(0, 0));
    }

    public HashMap<String, Dimension> getWindowDimensions() { return windowDimensions; }
    public void setWindowDimensions(HashMap<String, Dimension> windowDimensions) { this.windowDimensions = windowDimensions; }

    public HashMap<String, Point> getWindowLocations() { return windowLocations; }
    public void setWindowLocations(HashMap<String, Point> windowLocations) { this.windowLocations = windowLocations; }

    public boolean isShowCamRotationWidget() { return showCamRotationWidget; }
    public void setShowCamRotationWidget(boolean showCamRotationWidget) { this.showCamRotationWidget = showCamRotationWidget; }

    public boolean isShowDebugLightsWindow() { return showDebugLightsWindow; }
    public void setShowDebugLightsWindow(boolean showDebugLightsWindow) { this.showDebugLightsWindow = showDebugLightsWindow; }

    @Transient
    public Point getWindowLocation(String name) {
        return windowLocations.get(name);
    }

    @Transient
    public void setWindowLocation(String name, Point point) {
        windowLocations.put(name, point);
    }

    @Transient
    public Dimension getWindowDimensions(String name) {
        return windowDimensions.get(name);
    }

    @Transient
    public void setWindowDimensions(String name, Dimension dimension) {
        windowDimensions.put(name, dimension);
    }



}
