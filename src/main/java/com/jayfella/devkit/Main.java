package com.jayfella.devkit;

import com.jayfella.devkit.config.DevKitConfig;
import com.jayfella.devkit.forms.MainPage;
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
import com.jayfella.devkit.swing.MainMenu;
import com.jayfella.devkit.swing.SwingTheme;
import com.jayfella.devkit.swing.WindowLocationSaver;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeSystem;
import com.jme3.system.awt.AwtPanel;
import com.jme3.system.awt.AwtPanelsContext;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {

    LOGGER.info("Engine Version: {}", JmeSystem.getFullName());
    LOGGER.info(
        "Operating System: {} {}", System.getProperty("os.name"), System.getProperty("os.arch"));
    // Enable font anti-aliasing. On my setup (manjaro linux) this is definitely required.
    // I'll have to do further research to see if this has adverse effects on other systems.
    // System.setProperty("awt.useSystemAAFontSettings","on");
    // cleartype - I think this one looks better.
    System.setProperty("awt.useSystemAAFontSettings", "lcd");

    // set the theme.
    SwingTheme.setTheme(DevKitConfig.getInstance().getSdkConfig().getTheme());

    JmeEngineService engineService = ServiceManager.registerService(JmeEngineServiceImpl.class);
    engineService.setShowSettings(false);
    AppSettings settings = new AppSettings(true);
    settings.setCustomRenderer(AwtPanelsContext.class);
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

    // The first "pass" involves just setting up the environment. Do as little as possible just so we can get the
    // window visible.
    SwingUtilities.invokeLater(() -> {

      // register all of our services...
      // All of these services are created on the AWT thread.

      ServiceManager.registerService(EventService.class);

      // Why is this being set?
      JPopupMenu.setDefaultLightWeightPopupEnabled(false);

      String parentDirName = new File(System.getProperty("user.dir")).getName();

      JFrame frame = new JFrame("JmeDevKit: " + parentDirName);
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosed(WindowEvent e) {
          ServiceManager.stop();
        }
      });

      ServiceManager.registerService(new WindowService(frame));

      // initialize tree view
      JTree tree = new JTree();
      DefaultTreeCellRenderer renderer =
          new DefaultTreeCellRenderer();
      renderer.setLayout(new FlowLayout(FlowLayout.LEFT));
      tree.setCellRenderer(renderer);

      JScrollPane treeViewPane = new JScrollPane(tree);
      treeViewPane.setMinimumSize(new Dimension(100, 500));
      ServiceManager.registerService(SceneTreeService.class, tree);

      JMenuBar menu = new MainMenu(frame);
      frame.setJMenuBar(menu);
      ServiceManager.registerService(new MenuService(menu));

      // position and size the main window...
      Dimension dimension = DevKitConfig.getInstance().getSdkConfig()
          .getWindowDimensions(MainPage.WINDOW_ID);
      Point location = DevKitConfig.getInstance().getSdkConfig()
          .getWindowLocation(MainPage.WINDOW_ID);
      AwtPanel jmePanel = ServiceManager.getService(JmeEngineService.class)
          .getAWTPanel();
      jmePanel.setSize(dimension);
      jmePanel.attachTo(true, ServiceManager.getService(JmeEngineService.class).getViewPort());
      jmePanel.attachTo(true, ServiceManager.getService(JmeEngineService.class).getGuiViewPort());

      frame.addComponentListener(new ComponentListener() {
        @Override
        public void componentResized(ComponentEvent e) {

          JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);

          engineService.enqueue(engineService::applyCameraFrustumSizes);

          JFrame frame = (JFrame) e.getSource();
          Dimension newSize = frame.getSize();

          DevKitConfig.getInstance().getSdkConfig()
              .setWindowDimensions(MainPage.WINDOW_ID, newSize);
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

      // position and size the main window...

      if (location == null) {
        frame.setLocationRelativeTo(null);
      } else {
        frame.setLocation(location);
      }

      JPanel northArea = new JPanel();
      northArea.setLayout(new BoxLayout(northArea, BoxLayout.X_AXIS));
      northArea.add(new JButton("toolbarButton"));
      northArea.add(new JLabel("NORTH"));
      northArea.setMinimumSize(new Dimension(-1, 100));

      JPanel leftSide = new JPanel(new FlowLayout(FlowLayout.LEFT));
      JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      JPanel rightSide = new JPanel(new FlowLayout(FlowLayout.LEFT));
      JSplitPane rightSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
          centerPanel, rightSide);
      rightSplitPane.setAlignmentX(Component.LEFT_ALIGNMENT);
      rightSplitPane.setOneTouchExpandable(true);
      rightSplitPane.setContinuousLayout(true);

      JSplitPane leftSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
          leftSide, rightSplitPane);
      leftSplitPane.setAlignmentX(Component.LEFT_ALIGNMENT);
      leftSplitPane.setOneTouchExpandable(true);
      leftSplitPane.setContinuousLayout(true);

      rightSide.setMinimumSize(new Dimension(100, 50));
      rightSide.setPreferredSize(new Dimension(200, 500));
      PropertyInspectorService propertyInspectorService = new PropertyInspectorService();
      ServiceManager
          .registerService(PropertyInspectorService.class, propertyInspectorService);
      leftSide.setMinimumSize(new Dimension(100, 500));
      leftSide.setPreferredSize(new Dimension(200, 500));
      leftSide.add(treeViewPane);
      rightSide.add(propertyInspectorService.getSectionPanel());
      centerPanel.setPreferredSize(new Dimension(100, 500));
      centerPanel.setPreferredSize(new Dimension(200, 500));

      frame.getContentPane().add(new JButton("toolbarButton"), BorderLayout.NORTH);
      frame.getContentPane().add(new JLabel("SOUTH"), BorderLayout.SOUTH);
      frame.getContentPane().add(new JLabel("EAST"), BorderLayout.EAST);
      frame.getContentPane().add(new JLabel("WEST"), BorderLayout.WEST);
      // add the canvas AFTER we set the window size because the camera.getWidth and .getHeight values will change.
      // whilst it's easy to understand once you know, it can be confusing to figure this out.
      ImageIcon icon = new ImageIcon("images/middle.gif");
      JTabbedPane tabbedPane = new JTabbedPane();
      tabbedPane.setMinimumSize(new Dimension(100, 500));
      centerPanel.add(tabbedPane);
      AwtPanel awtPanel = ServiceManager.getService(JmeEngineService.class).getAWTPanel();
      awtPanel.setMinimumSize(new Dimension(100, 500));
      centerPanel.addComponentListener(new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
          frame.revalidate();

        }
      });
      //awtPanel.setPreferredSize(new Dimension(200, 500));
      tabbedPane
          .addTab("Tab 1", icon, awtPanel,
              "Does nothing");
      tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
      frame.add(leftSplitPane, BorderLayout.CENTER);
      frame.pack();
      // save any changes of movement and size to the configuration.
      // frame.addComponentListener(new WindowSizeAndLocationSaver(MainPage.WINDOW_ID));
      frame.addComponentListener(new WindowLocationSaver(MainPage.WINDOW_ID));
      //frame.addComponentListener(new WindowSizeSaver(MainPage.WINDOW_ID));

      // show the window.
      frame.setVisible(true);
      // verify that the asset root directory has been set properly.
      checkAssetRootDir(frame);

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
      //ServiceManager.getService(PluginService.class).loadPlugins();

    });

  }


  /**
   * Verifies the Asset Root directory. If an asset root has not been specified, it is defaulted to
   * "src/main/resources". A warning is displayed if the asset root directory does not exist.
   */
  private static void checkAssetRootDir(Frame frame) {

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
