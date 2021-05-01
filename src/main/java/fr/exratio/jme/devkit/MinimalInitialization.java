package fr.exratio.jme.devkit;

import com.jme3.system.AppSettings;
import com.jme3.system.awt.AwtPanelsContext;
import fr.exratio.jme.devkit.config.DevKitConfig;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import java.awt.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MinimalInitialization implements InitialisationTemplate {

  private static final Logger LOGGER = LoggerFactory.getLogger(MinimalInitialization.class);
  private Frame mainFrame;
  private final EditorJmeApplication editorJmeApplication;

  @Autowired
  public MinimalInitialization(
      EditorJmeApplication editorJmeApplication) {
    this.editorJmeApplication = editorJmeApplication;
  }


  @Override
  public void engineLoading() {
    editorJmeApplication.setShowSettings(false);
    AppSettings settings = new AppSettings(true);
    settings.setCustomRenderer(AwtPanelsContext.class);
    settings.setWidth(DevKitConfig.getInstance().getCameraDimension().width);
    settings.setHeight(DevKitConfig.getInstance().getCameraDimension().height);
    editorJmeApplication.setSettings(settings);

    editorJmeApplication.start(true);
    if (editorJmeApplication != null) {

      while (editorJmeApplication.getViewPort() == null) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          LOGGER.error("-- Main() Unexpected error occured", e);
        }
      }

    } else {
      LOGGER.info("Unable to create instance of JmeeditorJmeApplication. Exiting.");
      System.exit(-1);
    }
  }

  @Override
  public void GUILoading() {

  }

  @Override
  public void serviceLoading() {

  }

  @Override
  public void toolLoading() {

  }

  @Override
  public void appStateLoading() {

  }
}
