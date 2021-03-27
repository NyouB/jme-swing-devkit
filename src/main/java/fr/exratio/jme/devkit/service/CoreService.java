package fr.exratio.jme.devkit.service;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.system.awt.AwtPanel;
import fr.exratio.jme.devkit.config.DevKitConfig;
import fr.exratio.jme.devkit.forms.MainPage2;
import fr.exratio.jme.devkit.forms.MainPage2.Zone;
import fr.exratio.jme.devkit.forms.RunAppStateWindow;
import fr.exratio.jme.devkit.service.inspector.PropertyInspectorTool;
import fr.exratio.jme.devkit.swing.MainMenu;
import fr.exratio.jme.devkit.swing.WindowLocationSaver;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreService implements Service {

  private static final Logger LOGGER = LoggerFactory.getLogger(CoreService.class);
  private final long threadId;
  private final JFrame mainFrame;
  private final MainPage2 mainPage;

  public CoreService(String parentDirName) {
    mainFrame = new JFrame("JmeDevKit: " + parentDirName);
    threadId = Thread.currentThread().getId();
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

        JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);

        engineService.enqueue(engineService::applyCameraFrustumSizes);

        JFrame frame = (JFrame) e.getSource();
        Dimension newSize = frame.getSize();

        DevKitConfig.getInstance().getSdkConfig()
            .setWindowDimensions(MainPage2.WINDOW_ID, newSize);
        DevKitConfig.getInstance().save();

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
    mainPage = new MainPage2();
    JMenuBar menu = new MainMenu(mainFrame);
    ServiceManager.registerService(new MenuService(menu));
    ServiceManager.registerService(new ToolLocationService(mainFrame, mainPage));
    mainFrame.setJMenuBar(menu);
    mainFrame.setContentPane(mainPage);
    registerTools();

    // position and size the jme panel

    AwtPanel jmePanel = ServiceManager.getService(JmeEngineService.class)
        .getAWTPanel();
    jmePanel.setSize(DevKitConfig.getInstance().getCameraConfig().getCameraDimension());
    ImageIcon icon = new ImageIcon("images/middle.gif");
    mainPage.addTabCenterPanel("Canvas", jmePanel, icon);

    // add the canvas AFTER we set the window size because the camera.getWidth and .getHeight values will change.
    // whilst it's easy to understand once you know, it can be confusing to figure this out.

    // save any changes of movement and size to the configuration.
    // frame.addComponentListener(new WindowSizeAndLocationSaver(MainPage.WINDOW_ID));
    mainFrame.addComponentListener(new WindowLocationSaver(MainPage2.WINDOW_ID));
    //frame.addComponentListener(new WindowSizeSaver(MainPage.WINDOW_ID));
    mainFrame.pack();
    // show the window.
    mainFrame.setVisible(true);
    // verify that the asset root directory has been set properly.
    checkAssetRootDir(mainFrame);
  }

  private void registerTools() {

    SceneTreeService sceneTreeService = new SceneTreeService();
    ServiceManager.registerService(sceneTreeService);
    sceneTreeService.getZone().add(sceneTreeService);
    ServiceManager.getService(ToolLocationService.class).registerTool(sceneTreeService);

    RunAppStateWindow runAppStateWindow = new RunAppStateWindow();
    ServiceManager.getService(ToolLocationService.class).registerTool(runAppStateWindow);

    PropertyInspectorTool propertyInspectorTool = new PropertyInspectorTool();
    ServiceManager.registerService(propertyInspectorTool);
    ServiceManager.getService(ToolLocationService.class).registerTool(propertyInspectorTool);

  }

  /**
   * Verifies the Asset Root directory. If an asset root has not been specified, it is defaulted to
   * "src/main/resources". A warning is displayed if the asset root directory does not exist.
   */
  private void checkAssetRootDir(Frame frame) {

    // if the asset root dir is null, specify the default dir and notify the user.
    if (DevKitConfig.getInstance().getProjectConfig().getAssetRootDir() == null) {

      Path assetRoot = Paths.get("src", "main", "resources");
      DevKitConfig.getInstance().getProjectConfig()
          .setAssetRootDir(assetRoot.toAbsolutePath().toString());

      // @TODO: ask the user if they want to change it and navigate directly to it for them if true.

      JOptionPane.showMessageDialog(frame,
          "Your Asset Root directory has not been set and has been assigned a default value " +
              "of ./src/main/resources/"
              + System.lineSeparator()
              + "To change this value navigate to Edit -> Configuration.",
          "No Asset Root Specified",
          JOptionPane.INFORMATION_MESSAGE);

      DevKitConfig.getInstance().save();
    }

    // notify the user that the asset root dir doesn't exist.
    if (!new File(DevKitConfig.getInstance().getProjectConfig().getAssetRootDir()).exists()) {

      JOptionPane.showMessageDialog(frame,
          "The specified Asset Root directory does not exist. Please specify a valid Asset Root " +
              "directory by navigating to Edit - Configuration",
          "Invalid Asset Root",
          JOptionPane.WARNING_MESSAGE);

    } else {

      // create a file locator for the asset root dir.

      AssetManager assetManager = ServiceManager.getService(JmeEngineService.class)
          .getAssetManager();
      String assetRoot = DevKitConfig.getInstance().getProjectConfig().getAssetRootDir();

      assetManager.registerLocator(assetRoot, FileLocator.class);
      LOGGER.info("Registered Asset Root Directory: " + assetRoot);

    }

  }

  public MainPage2 getMainPage() {
    return mainPage;
  }

  @Override
  public long getThreadId() {
    return threadId;
  }

  @Override
  public void stop() {

  }

  public JFrame getMainFrame() {
    return mainFrame;
  }

}
