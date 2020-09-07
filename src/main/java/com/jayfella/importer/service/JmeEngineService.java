package com.jayfella.importer.service;

import com.jayfella.importer.jme.EditorCameraState;
import com.jayfella.importer.jme.SceneObjectHighlighterState;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioListenerState;
import com.jme3.environment.EnvironmentCamera;
import com.jme3.post.FilterPostProcessor;
import com.jme3.system.AppSettings;

import java.awt.*;

public abstract class JmeEngineService extends SimpleApplication implements Service {

    public JmeEngineService() {
        super(new AudioListenerState(),
                new EnvironmentCamera(),
                new EditorCameraState(),
                new SceneObjectHighlighterState()
        );

        AppSettings settings = new AppSettings(true);
        settings.setAudioRenderer(null);

        setPauseOnLostFocus(false);
        setSettings(settings);
        createCanvas();
        startCanvas();

    }

    public abstract FilterPostProcessor getFilterPostProcessor();
    public abstract void setFilterPostProcessor(FilterPostProcessor fpp);

    public abstract void applyCameraFrustumSizes();

    public abstract boolean isStarted();
    public abstract Canvas getCanvas();
}
