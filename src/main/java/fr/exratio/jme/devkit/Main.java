package fr.exratio.jme.devkit;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeSystem;
import com.jme3.system.awt.AwtPanel;
import com.jme3.system.awt.AwtPanelsContext;
import fr.exratio.jme.devkit.config.DevKitConfig;
import fr.exratio.jme.devkit.forms.RunAppStateWindow;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.MainPageController;
import fr.exratio.jme.devkit.service.PluginService;
import fr.exratio.jme.devkit.service.SceneTreeService;
import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.service.impl.EditorJmeApplicationImpl;
import fr.exratio.jme.devkit.service.inspector.PropertyInspectorTool;
import fr.exratio.jme.devkit.swing.MainMenu;
import fr.exratio.jme.devkit.swing.SwingTheme;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class Main {

  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
  private final EditorJmeApplication jmeEngineService;
  private final SceneTreeService sceneTreeService;
  private final PropertyInspectorTool propertyInspectorTool;
  private final DevKitConfig devKitConfig;
  private final PluginService pluginService;
  private final MainPageController mainPageController;
  private final RunAppStateWindow runAppStateWindow;
  private final JFrame mainFrame;
  private final MainMenu mainMenu;

  public static void main(String[] args) {
    // Enable font anti-aliasing. On my setup (manjaro linux) this is definitely required.
    // I'll have to do further research to see if this has adverse effects on other systems.
    // System.setProperty("awt.useSystemAAFontSettings","on");
    // cleartype - I think this one looks better.
    System.setProperty("awt.useSystemAAFontSettings", "lcd");
    // set the theme.
    SwingTheme.setTheme(DevKitConfig.getInstance().getTheme());
    AnnotationConfigApplicationContext context =
        new AnnotationConfigApplicationContext(SpringConfiguration.class);
    Main bean = context.getBean(Main.class);
    bean.run(args);
  }

  @Autowired
  public Main(EditorJmeApplicationImpl jmeEngineService,
      SceneTreeService sceneTreeService,
      PropertyInspectorTool propertyInspectorTool,
      DevKitConfig devKitConfig,
      PluginService pluginService, MainPageController mainPageController,
      RunAppStateWindow runAppStateWindow, MainMenu mainMenu) {
    this.jmeEngineService = jmeEngineService;
    this.sceneTreeService = sceneTreeService;
    this.propertyInspectorTool = propertyInspectorTool;
    this.devKitConfig = devKitConfig;
    this.pluginService = pluginService;
    this.mainPageController = mainPageController;
    this.runAppStateWindow = runAppStateWindow;
    this.mainMenu = mainMenu;
    mainFrame = new JFrame();
  }

  public void run(String... args) {

    LOGGER.info("Engine Version: {}", JmeSystem.getFullName());
    LOGGER.info(
        "Operating System: {} {}", System.getProperty("os.name"), System.getProperty("os.arch"));

    configureJMEEngine();
    startEngine();
    configureJFrame();

    // The first "pass" involves just setting up the environment. Do as little as possible just so we can get the
    // window visible.
    SwingUtilities.invokeLater(() -> {
      // Why is this being set?
      JPopupMenu.setDefaultLightWeightPopupEnabled(false);
      //fix the node being display only on resizing
      mainFrame.revalidate();
      mainFrame.pack();
      // show the window.
      mainFrame.setVisible(true);
    });
    registerTools();
    // verify that the asset root directory has been set properly.
    checkAssetRootDir();
    // load any available plugins.
    // I'm not sure where we should put this.
    pluginService.loadPlugins();
  }

  private void configureJFrame() {
    mainFrame.setTitle("JmeDevKit: " + devKitConfig.getTitle());
    mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    mainFrame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        ServiceManager.stop();
      }
    });
    mainFrame.addComponentListener(new ComponentListener() {
      @Override
      public void componentResized(ComponentEvent e) {
        jmeEngineService.enqueue(jmeEngineService::applyCameraFrustumSizes);
      }

      @Override
      public void componentMoved(ComponentEvent e) {

      }

      @Override
      public void componentShown(ComponentEvent e) {

      }

      @Override
      public void componentHidden(ComponentEvent e) {

      }
    });
    mainFrame.setJMenuBar(mainMenu);
    mainFrame.setContentPane(mainPageController.getMainPage());
    addJmePanel();
  }

  private void addJmePanel() {
    AwtPanel jmePanel = jmeEngineService.getAWTPanel();
    jmePanel.setSize(devKitConfig.getCameraDimension());
    ImageIcon icon = new ImageIcon("images/middle.gif");
    mainPageController.getMainPage().addTabCenterPanel("Canvas", jmePanel, icon);
  }


  private void startEngine() {
    jmeEngineService.start(true);

    while (jmeEngineService.getAWTPanel() == null) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        LOGGER.error("-- Main() Unexpected error occured", e);
      }
    }
  }

  private void configureJMEEngine() {
    jmeEngineService.setShowSettings(false);
    AppSettings settings = new AppSettings(true);
    settings.setCustomRenderer(AwtPanelsContext.class);
    settings.setWidth(devKitConfig.getCameraDimension().width);
    settings.setHeight(devKitConfig.getCameraDimension().height);
    jmeEngineService.setSettings(settings);
  }

  private void registerTools() {
    mainPageController.registerTool(sceneTreeService);
    mainPageController.registerTool(runAppStateWindow);
    mainPageController.registerTool(propertyInspectorTool);
  }


  /**
   * Verifies the Asset Root directory. If an asset root has not been specified, it is defaulted to
   * "src/main/resources". A warning is displayed if the asset root directory does not exist.
   */
  private void checkAssetRootDir() {

    // if the asset root dir is null, specify the default dir and notify the user.
    if (devKitConfig.getAssetRootDir() == null) {

      Path assetRoot = Paths.get("src", "main", "resources");
      devKitConfig.setAssetRootDir(assetRoot.toAbsolutePath().toString());

      JOptionPane.showMessageDialog(mainFrame,
          "Your Asset Root directory has not been set and has been assigned a default value " +
              "of ./src/main/resources/"
              + System.lineSeparator()
              + "To change this value navigate to Edit -> Configuration.",
          "No Asset Root Specified",
          JOptionPane.INFORMATION_MESSAGE);
    }

    // notify the user that the asset root dir doesn't exist.
    if (!new File(devKitConfig.getAssetRootDir()).exists()) {

      JOptionPane.showMessageDialog(mainFrame,
          "The specified Asset Root directory does not exist. Please specify a valid Asset Root " +
              "directory by navigating to Edit - Configuration",
          "Invalid Asset Root",
          JOptionPane.WARNING_MESSAGE);

    } else {
      // create a file locator for the asset root dir.
      AssetManager assetManager = jmeEngineService.getAssetManager();
      String assetRoot = devKitConfig.getAssetRootDir();
      assetManager.registerLocator(assetRoot, FileLocator.class);
      LOGGER.info("Registered Asset Root Directory: " + assetRoot);
    }

  }

}