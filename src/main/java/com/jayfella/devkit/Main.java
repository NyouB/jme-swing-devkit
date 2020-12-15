package com.jayfella.devkit;

import com.jayfella.devkit.config.DevKitConfig;
import com.jayfella.devkit.forms.Configuration;
import com.jayfella.devkit.forms.DebugLights;
import com.jayfella.devkit.forms.ImportModel;
import com.jayfella.devkit.forms.MainPage;
import com.jayfella.devkit.forms.RunAppStateWindow;
import com.jayfella.devkit.jme.AppStateUtils;
import com.jayfella.devkit.jme.CameraRotationWidgetState;
import com.jayfella.devkit.jme.DebugGridState;
import com.jayfella.devkit.service.ClipboardService;
import com.jayfella.devkit.service.EventService;
import com.jayfella.devkit.service.JmeEngineService;
import com.jayfella.devkit.service.MenuService;
import com.jayfella.devkit.service.PluginService;
import com.jayfella.devkit.service.RegistrationService;
import com.jayfella.devkit.service.SceneTreeService;
import com.jayfella.devkit.service.ServiceManager;
import com.jayfella.devkit.service.WindowService;
import com.jayfella.devkit.service.impl.JmeEngineServiceImpl;
import com.jayfella.devkit.service.inspector.PropertyInspectorService;
import com.jayfella.devkit.swing.SwingTheme;
import com.jayfella.devkit.swing.WindowLocationSaver;
import com.jayfella.devkit.swing.WindowSizeSaver;
import com.jme3.app.StatsAppState;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.system.JmeSystem;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import org.jdesktop.swingx.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  private JFrame frame;

  public Main() {

    // Enable font anti-aliasing. On my setup (manjaro linux) this is definitely required.
    // I'll have to do further research to see if this has adverse effects on other systems.
    // System.setProperty("awt.useSystemAAFontSettings","on");
    // cleartype - I think this one looks better.
    System.setProperty("awt.useSystemAAFontSettings", "lcd");

    // set the theme.
    SwingTheme.setTheme(DevKitConfig.getInstance().getSdkConfig().getTheme());

    JmeEngineService engineService = ServiceManager.registerService(JmeEngineServiceImpl.class);

    if (engineService != null) {

      while (!engineService.isStarted()) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          LOGGER.error("-- Main() Unexpected error occured", e);
        }
      }

      // start the canvas as early as possible.
      engineService.startCanvas(true);
    } else {
      LOGGER.info("Unable to create instance of JmeEngineService. Exiting.");
      System.exit(-1);
    }
  }

  public static void main(String[] args) {

    LOGGER.info("Engine Version: {}", JmeSystem.getFullName());
    LOGGER.info(
        "Operating System: {} {}", System.getProperty("os.name"), System.getProperty("os.arch"));

    Main main = new Main();
    main.start();
  }

  public void start() {

    // The first "pass" involves just setting up the environment. Do as little as possible just so we can get the
    // window visible.
    SwingUtilities.invokeLater(() -> {

      // register all of our services...
      // All of these services are created on the AWT thread.

      ServiceManager.registerService(EventService.class);

      // Why is this being set?
      JPopupMenu.setDefaultLightWeightPopupEnabled(false);

      String parentDirName = new File(System.getProperty("user.dir")).getName();

      frame = new JFrame("JmeDevKit: " + parentDirName);
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosed(WindowEvent e) {
          ServiceManager.stop();
        }
      });

      ServiceManager.registerService(new WindowService(frame));

      initializeTreeView();
      initializeInspectorService();

      JMenuBar menu = createMenu();
      frame.setJMenuBar(menu);
      ServiceManager.registerService(new MenuService(menu));

      // position and size the main window...
      Dimension dimension = DevKitConfig.getInstance().getSdkConfig()
          .getWindowDimensions(MainPage.WINDOW_ID);
      Point location = DevKitConfig.getInstance().getSdkConfig()
          .getWindowLocation(MainPage.WINDOW_ID);
      ServiceManager.getService(JmeEngineService.class).getCanvas().setSize(dimension);
      JTabbedPane tabbedPane = new JTabbedPane();
      frame.addComponentListener(new ComponentListener() {
        @Override
        public void componentResized(ComponentEvent e) {

/*
          JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);
          engineService.getCanvas().setSize(e.getComponent().getSize());

          engineService.enqueue(engineService::applyCameraFrustumSizes);

          JFrame frame = (JFrame) e.getSource();
          Dimension newSize = frame.getSize();

          DevKitConfig.getInstance().getSdkConfig()
              .setWindowDimensions(MainPage.WINDOW_ID, newSize);
          DevKitConfig.getInstance().save();
          */
          System.out
              .println(ServiceManager.getService(JmeEngineService.class).getCanvas().getLocation());
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

      // position and size the main window...

      if (location == null) {
        frame.setLocationRelativeTo(null);
      } else {
        frame.setLocation(location);
      }
      BorderLayout mainBorderLayout = new BorderLayout();
      frame.setLayout(mainBorderLayout);
      JPanel northArea = new JPanel();
      northArea.setLayout(new BoxLayout(northArea, BoxLayout.X_AXIS));
      northArea.add(new JButton("toolbarButton"));
      northArea.add(new JLabel("NORTH"));
      northArea.setMinimumSize(new Dimension(-1, 100));
      frame.add(new JButton("toolbarButton"), BorderLayout.NORTH);
      frame.add(new JLabel("SOUTH"), BorderLayout.SOUTH);
      frame.add(new JLabel("EAST"), BorderLayout.EAST);
      frame.add(new JLabel("WEST"), BorderLayout.WEST);
      // add the canvas AFTER we set the window size because the camera.getWidth and .getHeight values will change.
      // whilst it's easy to understand once you know, it can be confusing to figure this out.

      ImageIcon icon = new ImageIcon("images/middle.gif");

      tabbedPane
          .addTab("Tab 1", icon, ServiceManager.getService(JmeEngineService.class).getCanvas(),
              "Does nothing");
      tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
      frame.add(tabbedPane, BorderLayout.CENTER);
      frame.pack();
      System.out
          .println(ServiceManager.getService(JmeEngineService.class).getCanvas().getLocation());
      // save any changes of movement and size to the configuration.
      // frame.addComponentListener(new WindowSizeAndLocationSaver(MainPage.WINDOW_ID));
      frame.addComponentListener(new WindowLocationSaver(MainPage.WINDOW_ID));
      //frame.addComponentListener(new WindowSizeSaver(MainPage.WINDOW_ID));

      // show the window.
      frame.setVisible(true);
      // verify that the asset root directory has been set properly.
      checkAssetRootDir();

    });

    // now we have the window visible we can display progress data and load anything else we need.
    SwingUtilities.invokeLater(() -> {

      // register all of our services...
      // All of these services are created on the AWT thread.
      ServiceManager.registerService(RegistrationService.class);
      ServiceManager.registerService(ClipboardService.class);
      ServiceManager.registerService(PluginService.class);

      // load any available plugins.
      // I'm not sure where we should put this.
      ServiceManager.getService(PluginService.class).loadPlugins();

    });

  }

  private JMenuBar createMenu() {
    JMenuBar menuBar = new JMenuBar();

    // FILE menu
    JMenu fileMenu = menuBar.add(new JMenu("File"));

    JMenuItem importModelItem = fileMenu.add(new JMenuItem("Import Model..."));
    importModelItem.addActionListener(e -> {

      ImportModel importModel = new ImportModel();

      JDialog importModelDialog = new JDialog(frame, "Import Model", true);
      importModelDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      importModelDialog.setContentPane(importModel.$$$getRootComponent$$$());
      importModelDialog.pack();
      importModelDialog.setLocationRelativeTo(frame);

      importModelDialog.setVisible(true);

    });

    fileMenu.add(new JSeparator());

    JMenuItem exitMenuItem = fileMenu.add(new JMenuItem("Exit"));
    exitMenuItem.addActionListener(e -> {
      ServiceManager.getService(JmeEngineService.class).stop();
      frame.dispose();
    });

    // EDIT menu
    JMenu editMenu = menuBar.add(new JMenu("Edit"));

    JMenuItem configItem = editMenu.add(new JMenuItem("Configuration..."));
    configItem.addActionListener(e -> {

      Configuration configuration = new Configuration();

      JDialog configDialog = new JDialog(frame, Configuration.WINDOW_ID, true);
      configDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      configDialog.setContentPane(configuration.$$$getRootComponent$$$());
      configDialog.pack();
      configDialog.setLocationRelativeTo(frame);
      configDialog.setVisible(true);

    });

    // VIEW menu
    JMenu viewMenu = menuBar.add(new JMenu("View"));
    JCheckBoxMenuItem statsMenuItem = (JCheckBoxMenuItem) viewMenu
        .add(new JCheckBoxMenuItem("Statistics"));
    statsMenuItem.addActionListener(e -> {
      JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
      AppStateUtils.toggleAppState(StatsAppState.class, checkBoxMenuItem.isSelected());
    });

    JCheckBoxMenuItem camRotWidgetItem = (JCheckBoxMenuItem) viewMenu
        .add(new JCheckBoxMenuItem("Camera Rotation Widget"));
    camRotWidgetItem
        .setSelected(DevKitConfig.getInstance().getSdkConfig().isShowCamRotationWidget());
    camRotWidgetItem.addActionListener(e -> {
      JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
      final boolean isSelected = checkBoxMenuItem.isSelected();

      AppStateUtils.toggleAppState(CameraRotationWidgetState.class, isSelected);

      DevKitConfig.getInstance().getSdkConfig().setShowCamRotationWidget(isSelected);
      DevKitConfig.getInstance().save();
    });

    JCheckBoxMenuItem debugGridItem = (JCheckBoxMenuItem) viewMenu
        .add(new JCheckBoxMenuItem("Grid"));
    debugGridItem.setSelected(DevKitConfig.getInstance().getSceneConfig().isShowGrid());
    debugGridItem.addActionListener(e -> {
      JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
      final boolean isSelected = checkBoxMenuItem.isSelected();

      AppStateUtils.toggleAppState(DebugGridState.class, isSelected);

      DevKitConfig.getInstance().getSceneConfig().setShowGrid(isSelected);
      DevKitConfig.getInstance().save();

    });

    // WINDOW menu
    JMenu windowItem = menuBar.add(new JMenu("Window"));
    JCheckBoxMenuItem debugLightsItem = (JCheckBoxMenuItem) windowItem
        .add(new JCheckBoxMenuItem("Debug Lights"));
    debugLightsItem
        .setSelected(DevKitConfig.getInstance().getSdkConfig().isShowDebugLightsWindow());
    debugLightsItem.addActionListener(e -> {

      // A Dialog is still owned even if it is disposed. dispose() only affects a Window's displayability, not its ownership.
      // A window can be re-created after disposal by calling .setVisible(true) or .pack().

      // so we check if the main window still owns the dialog, and if it does, call .setVisible(true), else create it.

      Window window = ServiceManager.getService(WindowService.class)
          .getWindow(DebugLights.DEBUG_LIGHTS_WINDOW_TITLE);

      if (debugLightsItem.isSelected()) {

        if (window == null) {
          DebugLights debugLights = new DebugLights();

          JDialog dialog = ServiceManager.getService(WindowService.class)
              .createDialog(frame,
                  debugLights.$$$getRootComponent$$$(),
                  DebugLights.DEBUG_LIGHTS_WINDOW_TITLE,
                  true, true);

          dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
              debugLightsItem.setSelected(false);
            }
          });

          dialog.setVisible(true);

        } else {
          window.setVisible(true);
        }

      } else {

        if (window != null) {
          window.dispose();
        }

      }

      DevKitConfig.getInstance().getSdkConfig()
          .setShowDebugLightsWindow(debugLightsItem.isSelected());
      DevKitConfig.getInstance().save();

    });

    // bit of a strange place to put it, but we need to menu item to toggle if the window is closed.
    if (DevKitConfig.getInstance().getSdkConfig().isShowDebugLightsWindow()) {
      DebugLights debugLights = new DebugLights();

      JDialog dialog = ServiceManager.getService(WindowService.class)
          .createDialog(frame,
              debugLights.$$$getRootComponent$$$(),
              DebugLights.DEBUG_LIGHTS_WINDOW_TITLE,
              true, true);

      dialog.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosed(WindowEvent e) {
          debugLightsItem.setSelected(false);
        }
      });

      dialog.setVisible(true);
    }

    // AppState Window
    JCheckBoxMenuItem runAppStateItem = (JCheckBoxMenuItem) windowItem
        .add(new JCheckBoxMenuItem("Run AppState"));
    runAppStateItem
        .setSelected(DevKitConfig.getInstance().getSdkConfig().isShowRunAppStateWindow());
    runAppStateItem.addActionListener(e -> {

      Window window = ServiceManager.getService(WindowService.class)
          .getWindow(RunAppStateWindow.RUN_APPSTATE_WINDOW_TITLE);

      if (runAppStateItem.isSelected()) {

        if (window == null) {
          RunAppStateWindow runAppStateWindow = new RunAppStateWindow();

          JDialog dialog = ServiceManager.getService(WindowService.class)
              .createDialog(frame,
                  runAppStateWindow.$$$getRootComponent$$$(),
                  RunAppStateWindow.RUN_APPSTATE_WINDOW_TITLE,
                  true, true);

          dialog.setMinimumSize(new Dimension(400, 500));

          dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
              runAppStateItem.setSelected(false);
            }
          });

          dialog.setVisible(true);

        } else {
          window.setVisible(true);
        }

      } else {

        if (window != null) {
          window.dispose();
        }

      }

      DevKitConfig.getInstance().getSdkConfig()
          .setShowRunAppStateWindow(runAppStateItem.isSelected());
      DevKitConfig.getInstance().save();

    });

    // bit of a strange place to put it, but we need to menu item to toggle if the window is closed.
    if (DevKitConfig.getInstance().getSdkConfig().isShowRunAppStateWindow()) {
      RunAppStateWindow runAppStateWindow = new RunAppStateWindow();

      JDialog dialog = ServiceManager.getService(WindowService.class)
          .createDialog(frame,
              runAppStateWindow.$$$getRootComponent$$$(),
              RunAppStateWindow.RUN_APPSTATE_WINDOW_TITLE,
              true, true);

      dialog.setMinimumSize(new Dimension(400, 500));

      dialog.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosed(WindowEvent e) {
          runAppStateItem.setSelected(false);
        }
      });

      dialog.setVisible(true);
    }

    return menuBar;
  }

  private void initializeTreeView() {

    JTree tree = new JTree();

    JDialog treeViewFrame = new JDialog(frame, "Scene Tree");

    treeViewFrame.setContentPane(new JScrollPane(tree));
    treeViewFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    treeViewFrame.pack();

    ServiceManager.getService(WindowService.class)
        .positionWindowFromSavedPosition(treeViewFrame, SceneTreeService.WINDOW_ID);
    ServiceManager.getService(WindowService.class)
        .sizeWindowFromSavedSize(treeViewFrame, SceneTreeService.WINDOW_ID);

    treeViewFrame.addComponentListener(new WindowLocationSaver(SceneTreeService.WINDOW_ID));
    treeViewFrame.addComponentListener(new WindowSizeSaver(SceneTreeService.WINDOW_ID));

    treeViewFrame.setVisible(true);

    ServiceManager.registerService(SceneTreeService.class, tree);

  }

  private void initializeInspectorService() {

    JPanel panelRoot = new JPanel(new VerticalLayout());
    panelRoot.setBorder(new EmptyBorder(10, 10, 10, 10));

    JDialog inspectorFrame = new JDialog(frame, PropertyInspectorService.WINDOW_ID);
    inspectorFrame.setContentPane(new JScrollPane(panelRoot));
    inspectorFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

    ServiceManager.getService(WindowService.class)
        .positionWindowFromSavedPosition(inspectorFrame, PropertyInspectorService.WINDOW_ID);
    ServiceManager.getService(WindowService.class)
        .sizeWindowFromSavedSize(inspectorFrame, PropertyInspectorService.WINDOW_ID);

    inspectorFrame
        .addComponentListener(new WindowLocationSaver(PropertyInspectorService.WINDOW_ID));
    inspectorFrame.addComponentListener(new WindowSizeSaver(PropertyInspectorService.WINDOW_ID));

    inspectorFrame.setVisible(true);

    ServiceManager.registerService(PropertyInspectorService.class, panelRoot);

  }

  /**
   * Verifies the Asset Root directory. If an asset root has not been specified, it is defaulted to
   * "src/main/resources". A warning is displayed if the asset root directory does not exist.
   */
  private void checkAssetRootDir() {

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


}
