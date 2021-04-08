package fr.exratio.jme.devkit.service;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioListenerState;
import com.jme3.environment.EnvironmentCamera;
import com.jme3.post.FilterPostProcessor;
import com.jme3.system.awt.AwtPanel;
import devkit.appstate.tool.MouseOverAppState;
import devkit.appstate.tool.SpatialMoveToolState;
import devkit.appstate.tool.SpatialSelectorState;
import fr.exratio.jme.devkit.jme.EditorCameraState;
import fr.exratio.jme.devkit.jme.SceneObjectHighlighterState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public abstract class EditorJmeApplication extends SimpleApplication {

  public EditorJmeApplication() {
    super(new AudioListenerState(),
        new EnvironmentCamera(),
        new EditorCameraState(),
        new SceneObjectHighlighterState(),
        new MouseOverAppState()
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
