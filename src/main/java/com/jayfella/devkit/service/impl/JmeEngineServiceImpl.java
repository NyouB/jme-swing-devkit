package com.jayfella.devkit.service.impl;

import com.jayfella.devkit.config.CameraConfig;
import com.jayfella.devkit.config.DevKitConfig;
import com.jayfella.devkit.jme.CameraRotationWidgetState;
import com.jayfella.devkit.jme.DebugGridState;
import com.jayfella.devkit.service.JmeEngineService;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.TranslucentBucketFilter;
import com.jme3.system.JmeCanvasContext;
import com.jme3.util.MaterialDebugAppState;

import java.awt.*;
import java.util.logging.Logger;

public class JmeEngineServiceImpl extends JmeEngineService {

    private static final Logger log = Logger.getLogger(JmeEngineServiceImpl.class.getName());

    private volatile boolean started = false;
    private Canvas canvas;

    private FilterPostProcessor fpp;

    // let this service be accessed by any thread (so we can use .enqueue anywhere).
    private final long threadId = -1;

    @Override
    public FilterPostProcessor getFilterPostProcessor() {
        return fpp;
    }

    @Override
    public void setFilterPostProcessor(FilterPostProcessor fpp) {

        if (this.fpp != null) {
            viewPort.removeProcessor(this.fpp);
        }

        this.fpp = fpp;

        if (this.fpp != null) {
            viewPort.addProcessor(this.fpp);
        }

    }

    @Override
    public void simpleInitApp() {

        if (DevKitConfig.getInstance().getSdkConfig().isShowCamRotationWidget()) {
            stateManager.attach(new CameraRotationWidgetState());
        }

        if (DevKitConfig.getInstance().getSceneConfig().isShowGrid()) {
            stateManager.attach(new DebugGridState());
        }

        MaterialDebugAppState materialDebugAppState = new MaterialDebugAppState();
        stateManager.attach(materialDebugAppState);

        viewPort.setBackgroundColor(DevKitConfig.getInstance().getCameraConfig().getViewportColor());
        applyCameraFrustumSizes();

        canvas = createAwtCanvas();

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(new TranslucentBucketFilter());

        setFilterPostProcessor(fpp);

        started = true;
    }

    /**
     * Applies any changes made to the DevKitConfig camera configuration.
     * Used internally.
     */
    @Override
    public void applyCameraFrustumSizes() {

        CameraConfig cameraConfig = DevKitConfig.getInstance().getCameraConfig();

        float width, height;

        if (getCanvas() != null) {
            width = getCanvas().getWidth();
            height = getCanvas().getHeight();
        }
        else {
            width = viewPort.getCamera().getWidth();
            height = viewPort.getCamera().getHeight();
        }

        viewPort.getCamera().setFrustumPerspective(
                cameraConfig.getFieldOfView(),
                width / height,
                cameraConfig.getFrustumNear(),
                cameraConfig.getFrustumFar());

    }

    private Canvas createAwtCanvas(){

        JmeCanvasContext context = (JmeCanvasContext) getContext();
        Canvas canvas = context.getCanvas();

        return canvas;
    }

    @Override
    public synchronized boolean isStarted() {
        return started;
    }

    @Override
    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public long getThreadId() {
        return threadId;
    }

}
