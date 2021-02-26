package fr.exratio.devkit.service;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioListenerState;
import com.jme3.environment.EnvironmentCamera;
import com.jme3.post.FilterPostProcessor;
import com.jme3.system.awt.AwtPanel;
import fr.exratio.devkit.jme.EditorCameraState;
import fr.exratio.devkit.jme.SceneObjectHighlighterState;

public abstract class JmeEngineService extends SimpleApplication implements Service {

  public JmeEngineService() {
    super(new AudioListenerState(),
        new EnvironmentCamera(),
        new EditorCameraState(),
        new SceneObjectHighlighterState()
    );
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
   * Returns the AWT canvas.
   *
   * @return the AWT canvas.
   */
  public abstract AwtPanel getAWTPanel();

}
