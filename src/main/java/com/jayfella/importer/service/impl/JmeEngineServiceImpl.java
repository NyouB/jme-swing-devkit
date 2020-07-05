package com.jayfella.importer.service.impl;

import com.jayfella.importer.config.CameraConfig;
import com.jayfella.importer.config.DevKitConfig;
import com.jayfella.importer.jme.AppStateUtils;
import com.jayfella.importer.jme.BulletPhysics;
import com.jayfella.importer.jme.CameraRotationWidgetState;
import com.jayfella.importer.jme.DebugGridState;
import com.jayfella.importer.service.JmeEngineService;
import com.jme3.app.state.AppState;
import com.jme3.post.FilterPostProcessor;
import com.jme3.system.JmeCanvasContext;

import java.awt.*;
import java.util.logging.Logger;

public class JmeEngineServiceImpl extends JmeEngineService {

    private static final Logger log = Logger.getLogger(JmeEngineServiceImpl.class.getName());

    private volatile boolean started = false;
    private Canvas canvas;

    @Override
    public FilterPostProcessor getFilterPostProcessor() {
        return null;
    }

    @Override
    public void setFilterPostProcessor(FilterPostProcessor fpp) {

    }

    @Override
    public void simpleInitApp() {

        if (AppStateUtils.isBulletPhysicsOnClassPath()) {
            log.info("Bullet Physics Detected.");
            AppState bulletAppState = BulletPhysics.createAppState();
            stateManager.attach(bulletAppState);

        }

        if (DevKitConfig.getInstance().getSdkConfig().isShowCamRotationWidget()) {
            stateManager.attach(new CameraRotationWidgetState());
        }

        if (DevKitConfig.getInstance().getSceneConfig().isShowGrid()) {
            stateManager.attach(new DebugGridState());
        }

        viewPort.setBackgroundColor(DevKitConfig.getInstance().getCameraConfig().getViewportColor());
        applyCameraFrustumSizes();

        canvas = createAwtCanvas();

        started = true;
    }

    @Override
    public void applyCameraFrustumSizes() {

        CameraConfig cameraConfig = DevKitConfig.getInstance().getCameraConfig();

        viewPort.getCamera().setFrustumPerspective(
                cameraConfig.getFieldOfView(),
                (float) viewPort.getCamera().getWidth() / (float) viewPort.getCamera().getHeight(),
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

}
