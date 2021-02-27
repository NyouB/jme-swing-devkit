package fr.exratio.jme.devkit.service;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.system.awt.AwtPanel;
import fr.exratio.jme.devkit.config.DevKitConfig;
import fr.exratio.jme.devkit.forms.MainPage;
import fr.exratio.jme.devkit.service.inspector.PropertyInspectorService;
import fr.exratio.jme.devkit.swing.JSplitPaneWithZeroSizeDivider;
import fr.exratio.jme.devkit.swing.MainMenu;
import fr.exratio.jme.devkit.swing.WindowLocationSaver;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Point;
import java.awt.ScrollPane;
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
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import org.jdesktop.swingx.JXLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreServiceBack implements Service {

  private static final Logger LOGGER = LoggerFactory.getLogger(CoreServiceBack.class);
  private final JTabbedPane southTabPane;
  private final long threadId;
  private final JTabbedPane leftTabPane;
  private final JFrame mainFrame;

  public CoreServiceBack(String parentDirName) {
    mainFrame = new JFrame("JmeDevKit: " + parentDirName);
    threadId = Thread.currentThread().getId();
    mainFrame.setSize(DevKitConfig.getInstance().getSdkConfig()
        .getWindowDimensions(MainPage.WINDOW_ID));
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
    ServiceManager.registerService(new WindowService(mainFrame));
    JMenuBar menu = new MainMenu(mainFrame);
    ServiceManager.registerService(new MenuService(menu));
    mainFrame.setJMenuBar(menu);

    // position and size the main window...
    Point location = DevKitConfig.getInstance().getSdkConfig()
        .getWindowLocation(MainPage.WINDOW_ID);
    if (location == null) {
      mainFrame.setLocationRelativeTo(null);
    } else {
      mainFrame.setLocation(location);
    }

    mainFrame.getContentPane().add(new JButton("toolbarButton"), BorderLayout.NORTH);
    mainFrame.getContentPane().add(new JLabel("EAST"), BorderLayout.EAST);
    mainFrame.getContentPane().add(new JLabel("WEST"), BorderLayout.WEST);
    mainFrame.getContentPane().add(new JLabel("SOUTH"), BorderLayout.SOUTH);

    // position and size the jme panel

    AwtPanel jmePanel = ServiceManager.getService(JmeEngineService.class)
        .getAWTPanel();
    jmePanel.setSize(DevKitConfig.getInstance().getCameraConfig().getCameraDimension());
    jmePanel.attachTo(true, ServiceManager.getService(JmeEngineService.class).getViewPort());
    jmePanel.attachTo(true, ServiceManager.getService(JmeEngineService.class).getGuiViewPort());

    JPanel northArea = new JPanel();
    northArea.setLayout(new BoxLayout(northArea, BoxLayout.X_AXIS));
    northArea.add(new JButton("toolbarButton"));
    northArea.add(new JLabel("NORTH"));
    northArea.setMinimumSize(new Dimension(-1, 100));

    JPanel leftSide = new JPanel(new BorderLayout());
    JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JPanel rightSide = new JPanel(new BorderLayout());
    /*
    leftSide.setBorder(BorderFactory.createMatteBorder(
        5, 5, 5, 5, Color.red));
    centerPanel.setBorder(BorderFactory.createMatteBorder(
        5, 5, 5, 5, Color.blue));
    southPanel.setBorder(BorderFactory.createMatteBorder(
        5, 5, 5, 5, Color.white));
    rightSide.setBorder(BorderFactory.createMatteBorder(
        5, 5, 5, 5, Color.green));
        /*
     */
    JSplitPane rightSplitPane = new JSplitPaneWithZeroSizeDivider(JSplitPane.HORIZONTAL_SPLIT);
    rightSplitPane.setLeftComponent(centerPanel);
    rightSplitPane.setRightComponent(rightSide);
    rightSplitPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    rightSplitPane.setOneTouchExpandable(true);
    rightSplitPane.setContinuousLayout(true);

    JSplitPane leftSplitPane = new JSplitPaneWithZeroSizeDivider(JSplitPane.HORIZONTAL_SPLIT);
    leftSplitPane.setLeftComponent(leftSide);
    leftSplitPane.setRightComponent(rightSplitPane);
    leftSplitPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    leftSplitPane.setOneTouchExpandable(true);
    leftSplitPane.setContinuousLayout(true);

    JSplitPane southSplitPane = new JSplitPaneWithZeroSizeDivider(JSplitPane.VERTICAL_SPLIT);
    southSplitPane.setLeftComponent(leftSplitPane);
    southTabPane = new JTabbedPane(JTabbedPane.TOP);
    southPanel.add(southTabPane);
    southSplitPane.setRightComponent(southPanel);

    rightSide.setMinimumSize(new Dimension(100, 50));
    rightSide.setPreferredSize(new Dimension(200, 500));

    PropertyInspectorService propertyInspectorService = ServiceManager
        .registerService(PropertyInspectorService.class);

    SceneTreeService sceneTreeService = ServiceManager.registerService(SceneTreeService.class);
    leftSide.setMinimumSize(new Dimension(100, 500));
    leftSide.setPreferredSize(new Dimension(200, 500));
    leftTabPane = new JTabbedPane(JTabbedPane.LEFT);
    String treeViewTabTitle = "TreeView";
    addTabToLeftPane(treeViewTabTitle, sceneTreeService.getRootComponent());
    leftSide.add(leftTabPane, BorderLayout.CENTER);
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.add(propertyInspectorService.getSectionPanel());
    rightSide.add(scrollPane, BorderLayout.CENTER);
    centerPanel.setPreferredSize(new Dimension(100, 500));
    centerPanel.setPreferredSize(new Dimension(200, 500));

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
        mainFrame.revalidate();

      }
    });

    tabbedPane
        .addTab("Tab 1", icon, awtPanel,
            "Does nothing");
    tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

    mainFrame.getContentPane().add(southSplitPane, BorderLayout.CENTER);

    addTabToLeftPane(treeViewTabTitle, sceneTreeService.getRootComponent());

    // save any changes of movement and size to the configuration.
    // frame.addComponentListener(new WindowSizeAndLocationSaver(MainPage.WINDOW_ID));
    mainFrame.addComponentListener(new WindowLocationSaver(MainPage.WINDOW_ID));
    //frame.addComponentListener(new WindowSizeSaver(MainPage.WINDOW_ID));
    mainFrame.pack();
    // show the window.
    mainFrame.setVisible(true);
    // verify that the asset root directory has been set properly.
    checkAssetRootDir(mainFrame);
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


  public void addTabToLeftPane(String title, Component content) {
    leftTabPane.addTab(title, content);
    int tabIndex = leftTabPane.indexOfTab(title);
    JXLabel tabLabel = new JXLabel(title);
    tabLabel.setTextRotation(3 * Math.PI / 2);
    leftTabPane.setTabComponentAt(tabIndex, tabLabel);
    mainFrame.repaint();
  }

  public void addTabToSouthPane(String title, Component content) {
    southTabPane.addTab(title, content);
    mainFrame.repaint();
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
