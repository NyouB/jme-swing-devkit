package fr.exratio.jme.devkit.service;

import com.jme3.system.awt.AwtPanel;
import fr.exratio.jme.devkit.config.DevKitConfig;
import fr.exratio.jme.devkit.forms.RunAppStateWindow;
import fr.exratio.jme.devkit.main.MainPage;
import fr.exratio.jme.devkit.service.inspector.PropertyInspectorTool;
import fr.exratio.jme.devkit.swing.MainMenu;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MainPageController {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainPageController.class);
  private final MainPage mainPage;
  private final EditorJmeApplication jmeEngineService;
  private final DevKitConfig devKitConfig;
  private final MainMenu mainMenu;
  private final ToolLocationService toolLocationService;
  private final SceneTreeService sceneTreeService;


  public MainPageController(@Autowired EditorJmeApplication jmeEngineService,
      @Autowired DevKitConfig devKitConfig, @Autowired MainMenu mainMenu,
      @Autowired ToolLocationService toolLocationService,
      @Autowired SceneTreeService sceneTreeService) {
    this.jmeEngineService = jmeEngineService;
    this.devKitConfig = devKitConfig;
    this.mainMenu = mainMenu;
    this.toolLocationService = toolLocationService;
    this.sceneTreeService = sceneTreeService;


    registerTools();

    // position and size the jme panel

    AwtPanel jmePanel = jmeEngineService.getAWTPanel();
    jmePanel.setSize(devKitConfig.getCameraDimension());
    ImageIcon icon = new ImageIcon("images/middle.gif");
    mainPage.addTabCenterPanel("Canvas", jmePanel, icon);

    // add the canvas AFTER we set the window size because the camera.getWidth and .getHeight values will change.
    // whilst it's easy to understand once you know, it can be confusing to figure this out.

    // save any changes of movement and size to the configuration.
    // frame.addComponentListener(new WindowSizeAndLocationSaver(MainPage.WINDOW_ID));

  }

  private void registerTools() {
    toolLocationService.registerTool(sceneTreeService);

    RunAppStateWindow runAppStateWindow = new RunAppStateWindow();
    toolLocationService.registerTool(runAppStateWindow);

    PropertyInspectorTool propertyInspectorTool = new PropertyInspectorTool(registrationService,
        eventBus, exactMatchFinder, inheritedMatchFinder, defaultMatchFinder);

    ServiceManager.registerService(propertyInspectorTool);
    toolLocationService.registerTool(propertyInspectorTool);

  }

  public MainPage getMainPage() {
    return mainPage;
  }

  public JFrame getMainFrame() {
    return mainFrame;
  }


}
