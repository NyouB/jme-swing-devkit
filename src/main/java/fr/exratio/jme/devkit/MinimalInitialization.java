package fr.exratio.jme.devkit;

import com.jme3.system.AppSettings;
import com.jme3.system.awt.AwtPanelsContext;
import fr.exratio.jme.devkit.config.DevKitConfig;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.service.impl.EditorJmeApplicationImpl;
import java.awt.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MinimalInitialization implements InitialisationTemplate {

  private static final Logger LOGGER = LoggerFactory.getLogger(MinimalInitialization.class);
  private Frame mainFrame;

  @Override
  public void engineLoading() {
    EditorJmeApplication engineService = ServiceManager.registerService(EditorJmeApplicationImpl.class);
    engineService.setShowSettings(false);
    AppSettings settings = new AppSettings(true);
    settings.setCustomRenderer(AwtPanelsContext.class);
    settings.setWidth(DevKitConfig.getInstance().getCameraDimension().width);
    settings.setHeight(DevKitConfig.getInstance().getCameraDimension().height);
    engineService.setSettings(settings);

    engineService.start(true);
    if (engineService != null) {

      while (engineService.getViewPort() == null) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          LOGGER.error("-- Main() Unexpected error occured", e);
        }
      }

    } else {
      LOGGER.info("Unable to create instance of JmeEngineService. Exiting.");
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
