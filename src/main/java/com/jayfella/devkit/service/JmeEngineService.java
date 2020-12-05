package com.jayfella.devkit.service;

import com.jayfella.devkit.jme.EditorCameraState;
import com.jayfella.devkit.jme.SceneObjectHighlighterState;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioListenerState;
import com.jme3.environment.EnvironmentCamera;
import com.jme3.post.FilterPostProcessor;
import com.jme3.system.AppSettings;
import java.awt.Canvas;

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

  /**
   * Returns the currently active FilterPostProcessor.
   *
   * @return the currently active FilterPostProcessor.
   */
  public abstract FilterPostProcessor getFilterPostProcessor();

  /**
   * Removes the existing FilterPostProcessor if one exists and adds the given FilterPostProcessor.
   *
   * @param fpp the FilterPostProcessor to set.
   */
  public abstract void setFilterPostProcessor(FilterPostProcessor fpp);

  public abstract void applyCameraFrustumSizes();

  /**
   * Determines whether or not the engine has completed its initialization phase and is ready to
   * use.
   *
   * @return whether or not the engine is ready to use.
   */
  public abstract boolean isStarted();

  /**
   * Returns the AWT canvas.
   *
   * @return the AWT canvas.
   */
  public abstract Canvas getCanvas();

}
