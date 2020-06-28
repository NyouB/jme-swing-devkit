package com.jayfella.importer;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;
import com.jayfella.importer.config.DevKitConfig;
import com.jayfella.importer.core.LogUtil;
import com.jayfella.importer.forms.Configuration;
import com.jayfella.importer.forms.DebugLights;
import com.jayfella.importer.forms.ImportModel;
import com.jayfella.importer.forms.MainPage;
import com.jayfella.importer.jme.AppStateUtils;
import com.jayfella.importer.jme.CameraRotationWidgetState;
import com.jayfella.importer.jme.DebugGridState;
import com.jayfella.importer.service.JmeEngineService;
import com.jayfella.importer.service.SceneTreeService;
import com.jayfella.importer.service.ServiceManager;
import com.jayfella.importer.service.WindowService;
import com.jayfella.importer.service.impl.JmeEngineServiceImpl;
import com.jayfella.importer.swing.*;
import com.jme3.app.StatsAppState;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import org.apache.log4j.Level;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());

    private JFrame frame;
    // private final Canvas canvas;

    public static void main(String[] args){

        LogUtil.initializeLogger(Level.INFO, true);

        Main main = new Main();
        main.start();
    }

    public Main() {

        // @TODO: let the user choose light/dark mode.
        // LafManager.install(new IntelliJTheme()); // light
        LafManager.install(new DarculaTheme()); // dark
        // LafManager.reg

        ServiceManager.registerService(JmeEngineServiceImpl.class);
        ServiceManager.registerService(WindowService.class);

        JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);

        while (!engineService.isStarted()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // canvas = createCanvas(engineService);
    }

    public void start() {

        SwingUtilities.invokeLater(() -> {

            JPopupMenu.setDefaultLightWeightPopupEnabled(false);

            String parentDirName = new File(System.getProperty("user.dir")).getName();

            frame = new JFrame("JmeDevKit: " + parentDirName);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter(){
                @Override
                public void windowClosed(WindowEvent e) {
                    ServiceManager.stop();
                }
            });

            initializeTreeView(new JTree());

            JMenuBar menu = createMenu();
            frame.add(menu, BorderLayout.NORTH);

            // Should this be done on the AWT thread? The examples do... I'm not sure.
            ServiceManager.getService(JmeEngineService.class).startCanvas();

            // position and size the main window...
            Dimension dimension = DevKitConfig.getInstance().getSdkConfig().getWindowDimensions(MainPage.WINDOW_ID);
            Point location = DevKitConfig.getInstance().getSdkConfig().getWindowLocation(MainPage.WINDOW_ID);

            ServiceManager.getService(JmeEngineService.class).getCanvas().setSize(dimension);

            if (location == null) {
                frame.setLocationRelativeTo(null);
            }
            else {
                frame.setLocation(location);
            }

            // add the canvas AFTER we set the window size because the camera.getWidth and .getHeight values will change.
            // whilst it's easy to understand once you know, it can be confusing to figure this out.
            frame.add(ServiceManager.getService(JmeEngineService.class).getCanvas(), BorderLayout.CENTER);
            frame.pack();

            // save any changes of movement and size to the configuration.
            // frame.addComponentListener(new WindowSizeAndLocationSaver(MainPage.WINDOW_ID));
            frame.addComponentListener(new WindowLocationSaver(MainPage.WINDOW_ID));
            ServiceManager.getService(JmeEngineService.class).getCanvas().addComponentListener(new JmeCanvasSizeSaver());

            // show the window.
            frame.setVisible(true);

            // verify that the asset root directory has been set properly.
            checkAssetRootDir();

        });

    }

    private JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();

        // FILE menu
        JMenu fileMenu = menuBar.add(new JMenu("File"));

        JMenuItem importModelItem = fileMenu.add(new JMenuItem("Import Model..."));
        importModelItem.addActionListener(e -> {

            ImportModel importModel = new ImportModel();

            JFrame jFrame = new JFrame("Import Model");
            jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            jFrame.setContentPane(importModel.$$$getRootComponent$$$());
            jFrame.addWindowListener(new WindowServiceListener());
            jFrame.pack();
            jFrame.setLocationRelativeTo(jFrame);

            jFrame.addWindowListener(new WindowServiceListener());
            jFrame.setVisible(true);

            ServiceManager.getService(WindowService.class).add(jFrame);
        });

        fileMenu.add(new JSeparator());

        JMenuItem exitMenuItem = fileMenu.add(new JMenuItem("Exit"));
        exitMenuItem.addActionListener(e -> {
            // simpleApplication.stop();
            ServiceManager.getService(JmeEngineService.class).stop();
            frame.dispose();
        });

        // EDIT menu
        JMenu editMenu = menuBar.add(new JMenu("Edit"));

        JMenuItem configItem = editMenu.add(new JMenuItem("Configuration..."));
        configItem.addActionListener(e -> {

            Window window = ServiceManager.getService(WindowService.class).getWindow(Configuration.WINDOW_ID);

            if (window == null) {
                Configuration configuration = new Configuration();

                JFrame frame = new JFrame(Configuration.WINDOW_ID);
                frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                frame.setContentPane(configuration.$$$getRootComponent$$$());
                frame.addWindowListener(new WindowServiceListener());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
            else {
                window.toFront();
            }

        });

        // VIEW menu
        JMenu viewMenu = menuBar.add(new JMenu("View"));
        JCheckBoxMenuItem statsMenuItem = (JCheckBoxMenuItem) viewMenu.add(new JCheckBoxMenuItem("Statistics"));
        statsMenuItem.addActionListener(e -> {
            JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
            AppStateUtils.toggleAppState(StatsAppState.class, checkBoxMenuItem.isSelected());
        });

        JCheckBoxMenuItem camRotWidgetItem = (JCheckBoxMenuItem) viewMenu.add(new JCheckBoxMenuItem("Camera Rotation Widget"));
        camRotWidgetItem.setSelected(DevKitConfig.getInstance().getSdkConfig().isShowCamRotationWidget());
        camRotWidgetItem.addActionListener(e -> {
            JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
            final boolean isSelected = checkBoxMenuItem.isSelected();

            AppStateUtils.toggleAppState(CameraRotationWidgetState.class, isSelected);

            DevKitConfig.getInstance().getSdkConfig().setShowCamRotationWidget(isSelected);
            DevKitConfig.getInstance().save();
        });

        JCheckBoxMenuItem debugGridItem = (JCheckBoxMenuItem) viewMenu.add(new JCheckBoxMenuItem("Grid"));
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
        JCheckBoxMenuItem debugLightsItem = (JCheckBoxMenuItem) windowItem.add(new JCheckBoxMenuItem("Debug Lights"));
        debugLightsItem.setSelected(DevKitConfig.getInstance().getSdkConfig().isShowDebugLightsWindow());
        debugLightsItem.addActionListener(e -> {

            JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
            final boolean isSelected = checkBoxMenuItem.isSelected();

            Window window = ServiceManager.getService(WindowService.class).getWindow(DebugLights.DEBUG_LIGHTS_WINDOW_TITLE);

            if (isSelected) {

                if (window == null) {
                    JFrame frame = Windows.createDebugLightsWindow(checkBoxMenuItem);
                    frame.setVisible(true);
                }
                else {
                    window.toFront();
                }

            }
            else {

                if (window != null) {
                    window.dispose();
                }

            }

            DevKitConfig.getInstance().getSdkConfig().setShowDebugLightsWindow(isSelected);
            DevKitConfig.getInstance().save();

        });

        // bit of a strange place to put it, but we need to menu item to toggle if the window is closed.
        if (DevKitConfig.getInstance().getSdkConfig().isShowDebugLightsWindow()) {
            JFrame frame = Windows.createDebugLightsWindow(debugLightsItem);
            frame.setVisible(true);
        }

        return menuBar;
    }

    private void initializeTreeView(JTree tree) {

        JFrame treeViewFrame = new JFrame("Scene Tree");
        treeViewFrame.setContentPane(new JScrollPane(tree));
        treeViewFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        treeViewFrame.pack();


        ServiceManager.getService(WindowService.class).positionWindowFromSavedPosition(treeViewFrame, SceneTreeService.WINDOW_ID);
        ServiceManager.getService(WindowService.class).sizeWindowFromSavedSize(treeViewFrame, SceneTreeService.WINDOW_ID);

        treeViewFrame.addWindowListener(new WindowServiceListener());
        treeViewFrame.addComponentListener(new WindowLocationSaver(SceneTreeService.WINDOW_ID));
        treeViewFrame.addComponentListener(new WindowSizeSaver(SceneTreeService.WINDOW_ID));

        treeViewFrame.setVisible(true);

        ServiceManager.registerService(SceneTreeService.class, tree);

    }

    /**
     * Verifies the Asset Root directory. If an asset root has not been specified, it is defaulted to "src/main/resources".
     * A warning is displayed if the asset root directory does not exist.
     */
    private void checkAssetRootDir() {

        // if the asset root dir is null, specify the default dir and notify the user.
        if (DevKitConfig.getInstance().getProjectConfig().getAssetRootDir() == null) {

            Path assetRoot = Paths.get("src", "main", "resources");
            DevKitConfig.getInstance().getProjectConfig().setAssetRootDir(assetRoot.toAbsolutePath().toString());

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

        }

        else {

            // create a file locator for the asset root dir.

            AssetManager assetManager = ServiceManager.getService(JmeEngineService.class).getAssetManager();
            String assetRoot = DevKitConfig.getInstance().getProjectConfig().getAssetRootDir();

            assetManager.registerLocator(assetRoot, FileLocator.class);
            log.info("Registered Asset Root Directory: " + assetRoot);

        }

    }

}
